package why.no.`implicit`

import shapeless._
import shapeless.labelled.{FieldType, field}

trait MapDecoder[HL <: HList] {
  def toHl(map: Map[String, Any]): Either[String, HL]
}
object MapDecoder {

  implicit val nil: MapDecoder[HNil] = (_: Map[String, Any]) => Right(HNil)

  implicit def cons[S <: Symbol, V, T <: HList](implicit
      witness: Witness.Aux[S],
      typeable: Typeable[V],
      transformer: MapDecoder[T]
  ): MapDecoder[FieldType[S, V] :: T] =
    (map: Map[String, Any]) => {
      val name = witness.value.name

      def unwrap(any: Any) = any match {
        case seq: Seq[_] =>
          Either.cond(
            seq.size == 1,
            seq.head,
            s""""$any" cannot be treated as a single value"""
          )
        case v => Right(v)
      }

      for {
        any <- map
          .get(name)
          .toRight(s""""$name" not found in property map""")
        single <- unwrap(any)
        typed <- typeable
          .cast(single)
          .toRight(s""""$any" cannot be cast to ${typeable.describe}""")
        t <- transformer.toHl(map)
      } yield field[S](typed) :: t
    }

  def to[A](map: Map[String, Any]) = new MapDecoderH[A](map)
}

class MapDecoderH[A](map: Map[String, Any]) {

  def transform[R <: HList](implicit
      gen: LabelledGeneric.Aux[A, R],
      transformer: MapDecoder[R]
  ): Either[String, A] = transformer.toHl(map).map(gen.from)
}

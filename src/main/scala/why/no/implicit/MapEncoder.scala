package why.no.`implicit`

import shapeless.labelled.FieldType
import shapeless.{::, HList, HNil, LabelledGeneric, Witness}

trait MapEncoder[E] {
  def toMap(e: E): Map[String, Any]
}

object MapEncoder {

  implicit val hnilEncoder: MapEncoder[HNil] = (hnil: HNil) => Map()

  implicit val stringEncoder: MapEncoder[String] = (e: String) => Map("value" -> e)

  implicit val booleanEncoder: MapEncoder[Boolean] = (e: Boolean) => Map("value" -> e)

  implicit val intEncoder: MapEncoder[Int] = (e: Int) => Map("value" -> e)

  implicit val longEncoder: MapEncoder[Long] = (e: Long) => Map("value" -> e)

  implicit def hlistToMap[K <: Symbol, H, T <: HList](implicit
      witness: Witness.Aux[K],
      hEncoder: MapEncoder[H],
      tEncoder: MapEncoder[T]
  ): MapEncoder[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name
    (hlist: FieldType[K, H] :: T) => {
      val valueMap = hEncoder.toMap(hlist.head)
      val value = valueMap.values.head
      val tail = tEncoder.toMap(hlist.tail)
      tail + (fieldName -> value)
    }
  }

  def from[E](e: E) = new MapEncoderH[E](e)
}

class MapEncoderH[E](e: E) {
  def transform[HL <: HList](implicit
      lg: LabelledGeneric.Aux[E, HL],
      dfEncoder: MapEncoder[HL]
  ): Map[String, Any] =
    dfEncoder.toMap(lg.to(e))
}

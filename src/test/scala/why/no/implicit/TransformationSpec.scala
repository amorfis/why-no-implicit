package why.no.`implicit`

import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TransformationSpec extends AnyWordSpecLike with Matchers with EitherValues {

  "decoder" can {

    "enable automatic conversion" when {

      "using valid data" in {

        case class TargetData(
            groupId: String,
            validForAnalysis: Boolean,
            applicationId: Int
        )

        val map = Map(
          "groupId" -> "123456712345",
          "applicationId" -> 31,
          "validForAnalysis" -> true
        )

        val transformed = MapDecoder.to[TargetData](map).transform
        transformed.value shouldBe TargetData(
          "123456712345",
          validForAnalysis = true,
          31
        )

        val mapAgain = MapEncoder.from(transformed.value).transform
        mapAgain shouldBe map
      }
    }
  }
}

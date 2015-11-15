package components.purchase

/**
  * Created by Sari on 11/15/2015.
  */
case class Purchase (val id: Option[Long],
                     val userId: Long,
                     val status: String
                     //TODO: payment
                    )

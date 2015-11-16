package components.purchase

import java.util.concurrent.atomic.AtomicLong

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

/**
  * Created by Sari on 11/15/2015.
  */
trait PurchaseRepositoryComponent {
  val purchaseRepository: PurchaseRepository

  trait PurchaseRepository {

    def createPurchase(purchase: Purchase) : Purchase

    def updatePurchase(purchase: Purchase)

    def tryFindById(purchaseId: Long): Option[Purchase]

    def deletePurchase(purchaseId: Long)

  }
}

trait PurchaseRepositoryComponentImpl extends PurchaseRepositoryComponent {

  override val purchaseRepository = new PurchaseRepositoryImpl

  class PurchaseRepositoryImpl extends PurchaseRepository {

    val purchase = {
      get[Option[Long]]("id") ~
        get[Long]("userId") ~
        get[String]("status") map {
        case id~userId~status => Purchase(id, userId, status)
      }
    }
    override def createPurchase(purchase: Purchase): Purchase = {

      val generatedId : Option[Long] = DB.withConnection { implicit c =>
        SQL("insert into purchase (userId, status) values ({userId}, {status})").on(
          'userId -> purchase.userId,
          'status -> purchase.status
        ).executeInsert()
      }
      val createdPurchase = purchase.copy(id = generatedId)

      createdPurchase
    }

    override def updatePurchase(purchase: Purchase) {
      //INFO: ignore the user id
      DB.withConnection { implicit c =>
        SQL("update purchase set status={status} where id={id}").on(
          'status -> purchase.status,
          'id -> purchase.id
        ).executeUpdate()
      }
    }

    override def tryFindById(purchaseId: Long): Option[Purchase] = {

      val result = DB.withConnection { implicit c =>
        SQL("select * from purchase where id={id}").on(
          'id -> purchaseId
        ).as(purchase *)
      }

      result.lift(0)
    }

    override def deletePurchase(purchaseId: Long) {

      DB.withConnection { implicit c =>
        SQL("delete from purchase where id={id}").on(
          'id -> purchaseId
        ).executeUpdate()
      }

    }

  }

}


package components.cart

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

/**
  * Created by Sari on 11/15/2015.
  */
trait CartRepositoryComponent {
  val cartRepository: CartRepository

  trait CartRepository {

    def addCart(cart: Cart) : Cart

    def updateCart(cart: Cart)

    def tryFindById(purchaseId: Long): List[Cart]

    def tryFindById(purchaseId: Long, itemId: Long): Option[Cart]

    def deleteCart(purchaseId: Long, itemId: Long)

  }
}

trait CartRepositoryComponentImpl extends CartRepositoryComponent {
  override val cartRepository = new CartRepositoryImpl

  class CartRepositoryImpl extends CartRepository {

    val cart = {
      get[Long]("purchaseId") ~
        get[Long]("itemId") ~
        get[Int]("quantity") map {
        case purchaseId~itemId~quantity => Cart(purchaseId, itemId, quantity)
      }
    }

    override def addCart(newCart: Cart) : Cart = {

      DB.withConnection { implicit c =>
        SQL("insert into cart (purchaseId, itemId, quantity) values ({purchaseId}, {itemId}, {quantity})").on(
          'purchaseId -> newCart.purchaseId,
          'itemId -> newCart.itemId,
          'quantity -> newCart.quantity
        ).executeUpdate()
      }

      newCart
    }

    override def updateCart(newCart: Cart) {

      DB.withConnection { implicit c =>
        SQL("update cart set quantity={quantity} where purchaseId={purchaseId} and itemId={itemId}").on(
          'quantity -> newCart.quantity,
          'purchaseId -> newCart.purchaseId,
          'itemId -> newCart.itemId
        ).executeUpdate()
      }
    }

    override def tryFindById(purchaseId: Long): List[Cart] = {

      val result = DB.withConnection { implicit c =>
        SQL("select * from cart where purchaseId={purchaseId}").on(
          'purchaseId -> purchaseId
        ).as(cart *)
      }

      result
    }

    override def tryFindById(purchaseId: Long, itemId: Long): Option[Cart] = {

      val result = DB.withConnection { implicit c =>
        SQL("select * from cart where purchaseId={purchaseId} and itemId={itemId}").on(
          'purchaseId -> purchaseId,
          'itemId -> itemId
        ).as(cart *)
      }

      result.lift(0)
    }

    override def deleteCart(purchaseId: Long, itemId: Long) {

      DB.withConnection { implicit c =>
        SQL("delete from cart where purchaseId={purchaseId} and itemId={itemId}").on(
          'purchaseId -> purchaseId,
          'itemId -> itemId
        ).executeUpdate()
      }
    }

  }

}


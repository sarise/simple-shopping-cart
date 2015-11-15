package components.cart

/**
  * Created by Sari on 11/15/2015.
  */
trait CartServiceComponent {

    val cartService: CartService

    trait CartService {

        def addCart(cart: Cart)

        def updateCart(cart: Cart)

        def tryFindById(userId: Long) : List[Cart]

        def tryFindById(userId: Long, itemId: Long): Option[Cart]

        def deleteCart(userId: Long, itemId: Long)
    }
}

trait CartServiceComponentImpl extends CartServiceComponent{

    self: CartRepositoryComponent =>

    override val cartService = new CartServiceImpl

    class CartServiceImpl extends CartService {

      override def addCart(cart: Cart) {
        cartRepository.addCart(cart)
      }
      override def updateCart(cart: Cart) {
        cartRepository.updateCart(cart)
      }

      override def tryFindById(userId: Long): List[Cart] = {
        cartRepository.tryFindById(userId)
      }

      override def tryFindById(userId: Long, itemId: Long): Option[Cart] = {
        cartRepository.tryFindById(userId, itemId)
      }

      override def deleteCart(userId: Long, itemId: Long) {
        cartRepository.deleteCart(userId, itemId)
      }

    }
}

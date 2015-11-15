package controllers

import components.cart.{CartRepositoryComponentImpl, CartServiceComponentImpl, CartController}
import components.item.{ItemServiceComponentImpl, ItemRepositoryComponentImpl, ItemController}
import components.purchase.{PurchaseRepositoryComponentImpl, PurchaseServiceComponentImpl, PurchaseController}
import components.user.{UserServiceComponentImpl, UserRepositoryComponentImpl, UserController}
import play.api._
import play.api.mvc._

object Application extends UserController
                   with ItemController
                   with CartController
                   with PurchaseController
                   with UserServiceComponentImpl
                   with ItemServiceComponentImpl
                   with CartServiceComponentImpl
                   with PurchaseServiceComponentImpl
                   with UserRepositoryComponentImpl
                   with ItemRepositoryComponentImpl
                   with CartRepositoryComponentImpl
                   with PurchaseRepositoryComponentImpl{

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
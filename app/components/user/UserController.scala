package components.user

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by Sari on 11/15/2015.
  */
trait UserController extends Controller {
    self: UserServiceComponent =>

    implicit val userReads = (__ \ "email").read[String]
                                           .map(resource => UserResource(resource))

    implicit val userWrites = new Writes[User] {
        override def writes(user: User): JsValue = {
            Json.obj(
                "id" -> user.id,
                "email" -> user.email
            )
        }
    }

    def all = Action {
        var users = userService.all()
        Ok(Json.toJson(users))
    }

    def createUser = Action(parse.json) {request =>
        unmarshalUserResource(request, (resource: UserResource) => {
            val user = User(Option.empty,
                            resource.email)
            val createdUser = userService.createUser(user)
            Created(Json.toJson(createdUser)).withHeaders()
        })
    }

    def updateUser(id: Long) = Action(parse.json) {request =>
        unmarshalUserResource(request, (resource: UserResource) => {
            val user = User(Option(id),
                            resource.email)
            var updatedUser = userService.updateUser(user)
            Ok(Json.toJson(updatedUser))
        })
    }

    def findUserById(id: Long) = Action {
        val user = userService.tryFindById(id)
        if (user.isDefined) {
            Ok(Json.toJson(user))
        } else {
            NotFound
        }
    }

    def deleteUser(id: Long) = Action {
        userService.delete(id)
        NoContent
    }

    private def unmarshalUserResource(request: Request[JsValue],
                                      block: (UserResource) => Result): Result = {
        request.body.validate[UserResource].fold(
            valid = block,
            invalid = (e => {
                val error = e.mkString
                Logger.error(error)
                BadRequest(error)
            })
        )
    }
}

case class UserResource(val email: String)

package components.user

/**
  * Created by Sari on 11/15/2015.
  */
trait UserServiceComponent {

    val userService: UserService

    trait UserService {

        def all() : List[User]

        def createUser(user: User): User

        def updateUser(user: User) : Option[User]

        def tryFindById(id: Long): Option[User]

        def delete(id: Long)

    }

}

trait UserServiceComponentImpl extends UserServiceComponent {
    self: UserRepositoryComponent =>

    override val userService = new UserServiceImpl

    class UserServiceImpl extends UserService {

        override def all() : List[User] = {
            userRepository.all()
        }
        override def createUser(user: User): User = {
            userRepository.createUser(user)
        }

        override def updateUser(user: User): Option[User] = {
            userRepository.updateUser(user)
        }

        override def tryFindById(id: Long): Option[User] = {
            userRepository.tryFindById(id)
        }

        override def delete(id: Long) {
            userRepository.delete(id)
        }

    }
}

package components.item

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

/**
  * Created by Sari on 11/15/2015.
  */
trait ItemRepositoryComponent {
    val itemRepository: ItemRepository

    trait ItemRepository {

        def all() : List[Item]

        def createItem(item: Item): Item

        def updateItem(item: Item)

        def tryFindById(id: Long): Option[Item]

        def delete(id: Long)

    }
}

trait ItemRepositoryComponentImpl extends ItemRepositoryComponent {
    override val itemRepository = new ItemRepositoryImpl

    class ItemRepositoryImpl extends ItemRepository {

        val item = {
            get[Option[Long]]("id") ~
              get[String]("name") ~
              get[Long]("price") map {
                case id~name~price => Item(id, name, price)
            }
        }

        override def all(): List[Item] = DB.withConnection { implicit c =>
            SQL("select * from item").as(item *)
        }

        override def createItem(item: Item): Item = {

            val generatedId : Option[Long] = DB.withConnection { implicit c =>
                SQL("insert into item (name, price) values ({name}, {price})").on(
                    'name -> item.name,
                    'price -> item.price
                ).executeInsert()
            }
            val createdItem = item.copy(id = generatedId)
            createdItem
        }

        override def updateItem(item: Item) {

            DB.withConnection { implicit c =>
                SQL("update item set name={name}, price={price} where id={id}").on(
                    'name -> item.name,
                    'price -> item.price,
                    'id -> item.id
                ).executeUpdate()
            }

        }

        override def tryFindById(id: Long): Option[Item] = {

            val result = DB.withConnection { implicit c =>
                SQL("select * from item where id={id}").on(
                    'id -> id
                ).as(item *)
            }

            result.lift(0)
        }

        override def delete(id: Long) {

            DB.withConnection { implicit c =>
                SQL("delete from item where id={id}").on(
                    'id -> id
                ).executeUpdate()
            }
        }

    }

}

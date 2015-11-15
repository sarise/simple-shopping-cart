package components.item

/**
  * Created by Sari on 11/15/2015.
  */
trait ItemServiceComponent {

    val itemService: ItemService

    trait ItemService {

        def all() : List[Item]

        def createItem(item: Item): Item

        def updateItem(item: Item)

        def tryFindById(id: Long): Option[Item]

        def delete(id: Long)
    }
}

trait ItemServiceComponentImpl extends ItemServiceComponent {
    self: ItemRepositoryComponent =>

    override val itemService = new ItemServiceImpl

    class ItemServiceImpl extends ItemService {

        override def all() : List[Item] = {
            itemRepository.all()
        }

        override def createItem(item: Item): Item = {
            itemRepository.createItem(item)
        }

        override def updateItem(item: Item) {
            itemRepository.updateItem(item)
        }

        override def tryFindById(id: Long): Option[Item] = {
            itemRepository.tryFindById(id)
        }

        override def delete(id: Long) {
            itemRepository.delete(id)
        }

    }
}

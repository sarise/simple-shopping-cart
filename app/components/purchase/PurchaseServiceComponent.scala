package components.purchase

/**
  * Created by Sari on 11/15/2015.
  */
trait PurchaseServiceComponent {

  val purchaseService: PurchaseService

  trait PurchaseService {

    def createPurchase(purchase: Purchase) : Purchase

    def updatePurchase(purchase: Purchase)

    def tryFindById(purchaseId: Long) : Option[Purchase]

    def deletePurchase(purchaseId: Long)
  }
}

trait PurchaseServiceComponentImpl extends PurchaseServiceComponent{

  self: PurchaseRepositoryComponent =>

  override val purchaseService = new PurchaseServiceImpl

  class PurchaseServiceImpl extends PurchaseService {

    override def createPurchase(purchase: Purchase) : Purchase = {
      purchaseRepository.createPurchase(purchase)
    }
    override def updatePurchase(purchase: Purchase) {
      purchaseRepository.updatePurchase(purchase)
    }

    override def tryFindById(purchaseId: Long): Option[Purchase] = {
      purchaseRepository.tryFindById(purchaseId)
    }

    override def deletePurchase(purchaseId: Long) {
      purchaseRepository.deletePurchase(purchaseId)
    }

  }
}

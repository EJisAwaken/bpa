from django.urls import path
from .views import get_requisitions_controller,get_commandes_controller,get_factures_controller,get_facturecomp_controller,get_facturecomp_ett_controller,get_detailmere_controller,get_detail_commandes_controller,get_detail_requisitions_controller,update_commande_view,update_facture_view,update_requisition_view,update_article_view,get_requisition_approve_in_x3_controller,get_details_requisition_approve_in_x3_controller,get_commande_approve_in_x3_controller

urlpatterns = [
    path('demandes/', get_requisitions_controller),
    path('commandes/', get_commandes_controller),
    path('factures/', get_factures_controller),
    path('factures-comp/', get_facturecomp_controller),
    path('factures-comp-ett/', get_facturecomp_ett_controller),
    path('details-mere/', get_detailmere_controller),
    path('demandes-approve/', get_requisition_approve_in_x3_controller),
    path('commandes-approve/', get_commande_approve_in_x3_controller),
    path('details-da/', get_detail_requisitions_controller),
    path('details-bc/', get_detail_commandes_controller),
    path('details-approve/', get_details_requisition_approve_in_x3_controller),
    path('update_demande/', update_requisition_view),
    path('update_commande/', update_commande_view),
    path('update_article/', update_article_view),
    path('update_facture/', update_facture_view)
]

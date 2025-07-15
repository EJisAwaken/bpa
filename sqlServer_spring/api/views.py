from django.shortcuts import render

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
import json
from .service import get_requisitions,get_commandes,get_factures,get_facturecomp,get_facturecomp_ett,get_detail_requisitions,get_detailmere,get_detail_commandes,update_requisition,update_facture,update_commande,update_article,get_requisition_approve_in_x3,get_details_requisition_approve_in_x3,get_commande_approve_in_x3


def get_requisitions_controller(request):
    try:
        data = get_requisitions()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_commandes_controller(request):
    try:
        data = get_commandes()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_factures_controller(request):
    try:
        data = get_factures()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)
def get_detailmere_controller(request):
    try:
        data = get_detailmere()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_facturecomp_controller(request):
    try:
        data = get_facturecomp()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_facturecomp_ett_controller(request):
    try:
        data = get_facturecomp_ett()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_requisition_approve_in_x3_controller(request):
    try:
        data = get_requisition_approve_in_x3()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_commande_approve_in_x3_controller(request):
    try:
        data = get_commande_approve_in_x3()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_detail_requisitions_controller(request):
    try:
        data = get_detail_requisitions()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_detail_commandes_controller(request):
    try:
        data = get_detail_commandes()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)

def get_details_requisition_approve_in_x3_controller(request):
    try:
        data = get_details_requisition_approve_in_x3()
        return JsonResponse({
            "status": "success",
            "data": data,
            "count": len(data)
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": str(e)
        }, status=500)
    
@csrf_exempt
@require_http_methods(["PUT"])
def update_requisition_view(request):
    try:
        print("BODY >>>", request.body)  # Debug
        data = json.loads(request.body)
        result = update_requisition(data)
        return JsonResponse(result)
    except Exception as e:
        return JsonResponse({"status": "error", "message": str(e)}, status=500)

@csrf_exempt
@require_http_methods(["PUT"])
def update_commande_view(request):
    try:
        print("BODY >>>", request.body)  # Debug
        data = json.loads(request.body)
        result = update_commande(data)
        return JsonResponse(result)
    except Exception as e:
        return JsonResponse({"status": "error", "message": str(e)}, status=500)

@csrf_exempt
@require_http_methods(["PUT"])
def update_article_view(request):
    try:
        print("BODY >>>", request.body)
        data = json.loads(request.body)
        result = update_article(data)
        return JsonResponse(result)
    except Exception as e:
        return JsonResponse({"status": "error", "message": str(e)}, status=500)

@csrf_exempt
@require_http_methods(["PUT"])
def update_facture_view(request):
    try:
        print("BODY >>>", request.body)
        data = json.loads(request.body)
        result = update_facture(data)
        return JsonResponse(result)
    except Exception as e:
        return JsonResponse({"status": "error", "message": str(e)}, status=500)


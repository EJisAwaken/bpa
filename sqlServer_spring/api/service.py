from django.db import connection

# Listes des demandes
def get_requisitions():
    query = """
	SELECT DISTINCT 
        PREQUIS.PSHNUM_0 AS uid,
        AUTILIS.NOMUSR_0 AS demandeur,
        AUTILIS.ADDEML_0 AS email,
        PREQUIS.CPY_0 AS societe,
        SUM(PREQUISD.LINATIAMT_0) AS montant,
        AOBJTXT.NAM_0 AS lienPJ,
        AWRKHISSUI.DEST_0 AS validateur,
        AWRKHISSUI.ACTSIG_0,
        left(CAST(PREQUISD.CREDAT_0 as date),10) as date,
        CASE
            WHEN PREQUIS.APPFLG_0 = 1 THEN 'NON'
            WHEN PREQUIS.APPFLG_0 = 2 THEN 'PARTIELLEMENT'
            WHEN PREQUIS.APPFLG_0 = 3 THEN 'TOTALEMENT'
        END AS signature,
		PREQUIS.YBENEFIC_0 AS BENEFICIAIRE,
		val.NOMUSR_0 as nom_validateur,
		PREQUISD.CUR_0 AS devise,
		PREQUISD.PJT_0 as affaire
    FROM HABIBO.PREQUIS
    INNER JOIN HABIBO.PREQUISD on PREQUIS.PSHNUM_0 = PREQUISD.PSHNUM_0
    INNER JOIN HABIBO.AWRKHISSUI ON AWRKHISSUI.CLEOBJ_0 = PREQUIS.PSHNUM_0
    LEFT JOIN HABIBO.AOBJTXT ON PREQUIS.PSHNUM_0 = AOBJTXT.IDENT1_0
    INNER JOIN HABIBO.AUTILIS ON PREQUIS.REQUSR_0 = AUTILIS.USR_0
	INNER JOIN HABIBO.AUTILIS val on AWRKHISSUI.DEST_0 = val.USR_0
    WHERE PREQUIS.APPFLG_0 <> 3 AND AWRKHISSUI.ACTSIG_0 <> 'REJ' 
    GROUP BY 
        AUTILIS.NOMUSR_0, AUTILIS.ADDEML_0, PREQUIS.PSHNUM_0, PREQUIS.CPY_0,
        AOBJTXT.NAM_0, PREQUIS.APPFLG_0, AWRKHISSUI.DEST_0, AWRKHISSUI.ACTSIG_0,
	    PREQUISD.CREDAT_0,PREQUIS.YBENEFIC_0,val.NOMUSR_0,PREQUISD.CUR_0,PREQUISD.PJT_0
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "uid": r[0],
                "demandeur": r[1],
                "email": r[2],
                "societe": r[3],
                "montant": float(r[4]) if r[4] else 0,
                "lienPJ": r[5],
                "validateur": r[6],
                "actions": r[7],
                "date": r[8],
                "signature": r[9],
                "beneficiaire": r[10],
                "nom_validateur": r[11],
                "devise": r[12],
                "code_anal": r[13]
            }
            for r in rows
        ]
        return result

# Listes des commandes
def get_commandes():
    query = """
    select DISTINCT 
        PORDER.POHNUM_0 as 'N PIECE',
        AUTILIS.NOMUSR_0 as DEMANDEUR,
        AUTILIS.ADDEML_0 as 'E-MAIL',
        PORDER.CPY_0 as societe,
        sum(LINATI_0) as MONTANT,
        PORDER.CUR_0 as DEVISE,
        AOBJTXT.NAM_0 as 'Lien PJ',
        AWRKHISSUI.DEST_0 as code_validateur,
        left(CAST(PORDER.ORDDAT_0 as date),10) as date,
        CASE
            WHEN PORDER.APPFLG_0 = 1 THEN 'NON'
            WHEN PORDER.APPFLG_0 = 2 THEN 'PARTIELLEMENT'
            WHEN PORDER.APPFLG_0 = 3 THEN 'TOTALEMENT'
        END as SIGNATURE,
		PORDER.PTE_0 as Code_Paiement,
		ATEXTRA.TEXTE_0 as Mode_Paiement,
		PREQUIS.YBENEFIC_0 as BENEFICIAIRE, 
		val.NOMUSR_0 as nom_validateur,
		PREQUISD.PJT_0 as affaire,
		BPSUPPLIER.BPSNAM_0 as Fournisseur
    from HABIBO.PORDER 
    INNER JOIN HABIBO.AWRKHISSUI on AWRKHISSUI.CLEDEC_0 = PORDER.POHNUM_0
    INNER JOIN HABIBO.PORDERQ on PORDER.POHNUM_0 = PORDERQ.POHNUM_0
    INNER JOIN HABIBO.AUTILIS on PORDER.BUY_0 = AUTILIS.USR_0
	INNER JOIN HABIBO.AUTILIS val on AWRKHISSUI.DEST_0 = val.USR_0
    INNER JOIN HABIBO.ITMMASTER on PORDERQ.ITMREF_0 = ITMMASTER.ITMREF_0
    LEFT JOIN HABIBO.AOBJTXT on PORDER.POHNUM_0 = AOBJTXT.IDENT1_0
	inner JOIN HABIBO.TABPAYTERM on PORDER.PTE_0 = TABPAYTERM.PTE_0
	INNER JOIN HABIBO.PREQUISO on PORDERQ.POHNUM_0 = PREQUISO.POHNUM_0 and PORDERQ.POPLIN_0 = PREQUISO.POPLIN_0 and PORDERQ.POQSEQ_0 = PREQUISO.POQSEQ_0
	INNER JOIN HABIBO.PREQUISD on PREQUISO.PSHNUM_0 = PREQUISD.PSHNUM_0 
	INNER JOIN HABIBO.PREQUIS on PREQUISD.PSHNUM_0 = PREQUIS.PSHNUM_0 
	inner join HABIBO.ATEXTRA on TABPAYTERM.PTE_0 = ATEXTRA.IDENT1_0 and ZONE_0 = 'DESAXX' and LANGUE_0 = 'FRA'
	INNER JOIN HABIBO.BPSUPPLIER on PORDER.BPSNUM_0 = BPSUPPLIER.BPSNUM_0
    where  ABROBJ_0 = 'POH' and PORDER.APPFLG_0 <> 3 and FLGSIG_0 <>5 and AWRKHISSUI.DEST_0 <> 'ADMIN'
    GROUP BY
        PORDER.POHNUM_0,AUTILIS.NOMUSR_0,AUTILIS.ADDEML_0,PORDER.CPY_0,
        AOBJTXT.NAM_0,PORDER.APPFLG_0,AWRKHISSUI.VALCTX2_0,PORDER.CUR_0,
        AWRKHISSUI.USRSIG1_0,AWRKHISSUI.USRSIG2_0,ITMMASTER.TCLCOD_0,
        AWRKHISSUI.DEST_0,PORDER.ORDDAT_0,PORDER.PTE_0,ATEXTRA.TEXTE_0,
		PREQUIS.YBENEFIC_0,val.NOMUSR_0,PREQUISD.PJT_0,PREQUISD.CUR_0,BPSUPPLIER.BPSNAM_0
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "uid": r[0],
                "demandeur": r[1],
                "email": r[2],
                "societe": r[3],
                "montant": float(r[4]) if r[4] else 0,
                "devise": r[5],
                "lien_pj": r[6],
                "validateur": r[7],
                "date": r[8],
                "signature": r[9],
                "code_payement": r[10],
                "mode_payement": r[11],
                "beneficiaire": r[12],
                "nom_validateur": r[13],
                "code_anal": r[14],
                "fournisseur": r[15]
            }
            for r in rows
        ]
        return result

# Listes des facture mère
def get_factures():
    query = """
    select distinct 
        ligmer.NUM_0 as Facture_mere,
        ligmer.CPY_0 as Societe,
        entmer.BPRNAM_0 as Fournisseur,
        left(cast(entmer.CREDAT_0 as date),10) as Date,
        entmer.AMTATI_0 as Montant,
        entmer.CUR_0 as Devise
    from HABIBO.PINVOICED ligmer
        inner join HABIBO.PINVOICED ligcom on ligcom.NUMORI_0 = ligmer.NUM_0 and ligcom.LINORI_0 = ligmer.PIDLIN_0
        inner join HABIBO.PINVOICE entmer  on ligmer.NUM_0 = entmer.NUM_0
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "uid": r[0],
                "societe": r[1],
                "fournisseur": r[2],
                "date": r[3],
                "montant": float(r[4]) if r[4] else 0,
                "devise": r[5]
            }
            for r in rows
        ]
        return result

# Listes des details de la facture mère
def get_detailmere():
    query = """
    select distinct 
        ligmer.NUM_0 as Facture_mere,
        ligmer.CPY_0 as Societe,
        entmer.BPRNAM_0 as Fournisseur,
        left(cast(entmer.CREDAT_0 as date),10) as Date,
        ligmer.ITMREF_0 as Code,
        ligmer.ITMDES_0 as Artilce,
        ligmer.QTYSTU_0 as QTE,
        ligmer.NETPRI_0 as PU,
        ligmer.AMTATILIN_0 as Montant,
        entmer.CUR_0 as Devise 
    from HABIBO.PINVOICED ligmer
        inner join HABIBO.PINVOICED ligcom on ligcom.NUMORI_0 = ligmer.NUM_0 and ligcom.LINORI_0 = ligmer.PIDLIN_0
        inner join HABIBO.PINVOICE entmer  on ligmer.NUM_0 = entmer.NUM_0
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "uid": r[0],
                "societe": r[1],
                "fournisseur": r[2],
                "date": r[3],
                "code_article": r[4], 
                "article": r[5], 
                "quantite": float(r[6]) if r[6] else 0, 
                "pu": r[7], 
                "montant": float(r[8]) if r[8] else 0,
                "devise": r[9]
            }
            for r in rows
        ]
        return result
# Listes des factures complementaires
def get_facturecomp():
    query = """
    select distinct 
        entcom.NUM_0 as Facture_comple,
        entmer.NUM_0 as Mere,
        entcom.CPY_0 as Societe,
        entcom.BPRNAM_0 as Fournisseur,
        left(cast(entcom.CREDAT_0 as date),10) as Date,
        ligcom.ITMREF_0 as code,
        ligcom.ITMDES_0 as Article,
        entcom.AMTATI_0 as Montant, 
        entcom.PAZ_0 as etat,
        entcom.CUR_0 as Devise
    from HABIBO.PINVOICE entcom
        inner join HABIBO.PINVOICED ligcom on entcom.NUM_0 = ligcom.NUM_0
        inner join HABIBO.PINVOICED ligmer on ligcom.NUMORI_0 = ligmer.NUM_0 and ligcom.LINORI_0 = ligmer.PIDLIN_0
        inner join HABIBO.PINVOICE entmer on ligmer.NUM_0 = entmer.NUM_0
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "uid": r[0],
                "ref_dem": r[1],
                "societe": r[2],
                "fournisseur": r[3],
                "date": r[4], 
                "code": r[5],
                "article": r[6],
                "montant": float(r[7]) if r[7] else 0,
                "etat" : r[8],
                "devise": r[9]
            }
            for r in rows
        ]
        return result

# Listes entetes des factures complementaires
def get_facturecomp_ett():
    query = """
    select distinct
        entcom.NUM_0 as Facture_comple,
        entcom.CPY_0 as Societe,
        entcom.BPRNAM_0 as Fournisseur,
        left(cast(entcom.CREDAT_0 as date),10) as Date,
        entcom.AMTATI_0 as Montant, 
        entcom.CUR_0 as Devise
    from HABIBO.PINVOICE entcom
    where entcom.PIHTYP_0 = 2 
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "uid": r[0],
                "societe": r[1],
                "fournisseur": r[2],
                "date": r[3], 
                "montant": float(r[4]) if r[4] else 0,
                "devise": r[5]
            }
            for r in rows
        ]
        return result

# Listes des details des demandes
def get_detail_requisitions():
    query = """
    select DISTINCT 
        PREQUIS.PSHNUM_0 as num_piece,
        PREQUISD.ITMREF_0 AS code_article,
        PREQUISD.ITMDES_0 AS objet,
        PREQUISD.QTYPUU_0 as quantite,
        PREQUISD.LINATIAMT_0 as montant,
        (PREQUISD.LINATIAMT_0/PREQUISD.QTYPUU_0) as pu,
        AWRKHISSUI.DEST_0 as validateur,
        PREQUISD.CUR_0 AS devise,
        BPSUPPLIER.BPSNAM_0 as Fournisseur
    from HABIBO.PREQUISD 
    INNER JOIN HABIBO.AWRKHISSUI on AWRKHISSUI.CLEOBJ_0 = PREQUISD.PSHNUM_0 AND AWRKHISSUI.VALCTX4_0 = PREQUISD.ITMREF_0 
    INNER JOIN HABIBO.PREQUIS on PREQUISD.PSHNUM_0 = PREQUIS.PSHNUM_0
    INNER JOIN HABIBO.ITMMASTER on PREQUISD.ITMREF_0 = ITMMASTER.ITMREF_0
    LEFT JOIN HABIBO.AOBJTXT on PREQUIS.PSHNUM_0 = AOBJTXT.IDENT1_0
    INNER JOIN HABIBO.APLSTD on PREQUISD.YDIRECTION_0 = APLSTD.LANNUM_0 and LANCHP_0 = 6002
    INNER JOIN HABIBO.AUTILIS on PREQUIS.REQUSR_0 = AUTILIS.USR_0
    INNER JOIN HABIBO.BPSUPPLIER on PREQUISD.BPSNUM_0 = BPSUPPLIER.BPSNUM_0
    where  ABROBJ_0 = 'PSH' 
    and PREQUIS.APPFLG_0 <> 3
    GROUP BY 
        PREQUIS.PSHNUM_0,PREQUISD.ITMREF_0,PREQUISD.ITMDES_0,PREQUISD.QTYPUU_0,PREQUISD.LINATIAMT_0,
        AWRKHISSUI.DEST_0,PREQUISD.CUR_0,BPSUPPLIER.BPSNAM_0
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "num_piece": r[0],
                "code_article": r[1],
                "objet": r[2],
                "quantite": r[3],
                "pu": float(r[5]) if r[4] else 0,
                "montant": float(r[4]) if r[4] else 0,
                "validateur": r[6],
                "devise": r[7],
                "fournisseur": r[8]
            }
            for r in rows
        ]
        return result

# Listes des details des commandes
def get_detail_commandes():
    query = """
    select DISTINCT
        PORDER.POHNUM_0 as num_piece,
        PORDERQ.ITMREF_0 as code_article,
        ITMMASTER.ITMDES1_0 AS designation,
        PORDERQ.QTYSTU_0 as quantite,
        (LINATI_0/PORDERQ.QTYPUU_0) as pu,
        LINATI_0 as montant,
        PORDER.CUR_0 as devise
    from HABIBO.PORDERQ 
    INNER JOIN HABIBO.PORDER on PORDER.POHNUM_0 = PORDERQ.POHNUM_0
    INNER JOIN HABIBO.ITMMASTER on PORDERQ.ITMREF_0 = ITMMASTER.ITMREF_0
    where PORDER.APPFLG_0 <> 3 
    """
    
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "num_piece": r[0],
                "code_article": r[1],
                "objet": r[2],
                "quantite": r[3],
                "pu": float(r[4]) if r[5] else 0,
                "montant": float(r[5]) if r[5] else 0,
                "devise": r[6]
            }
            for r in rows
        ]
        return result
    

#--------- approuver sur SageX3------------
def get_requisition_approve_in_x3():
    query = """
    SELECT DISTINCT 
        PREQUIS.PSHNUM_0 AS uid,
        AWRKHISSUI.DEST_0 AS validateur,
	CASE
		WHEN PREQUIS.APPFLG_0 = 1 THEN 'N'
		WHEN PREQUIS.APPFLG_0 = 2 THEN 'P'
		WHEN PREQUIS.APPFLG_0 = 3 THEN 'T'
	END as action
    FROM HABIBO.PREQUIS
    INNER JOIN HABIBO.AWRKHISSUI ON AWRKHISSUI.CLEOBJ_0 = PREQUIS.PSHNUM_0
    LEFT JOIN HABIBO.AOBJTXT ON PREQUIS.PSHNUM_0 = AOBJTXT.IDENT1_0
    INNER JOIN HABIBO.AUTILIS ON PREQUIS.REQUSR_0 = AUTILIS.USR_0
    INNER JOIN HABIBO.PREQUISD on PREQUISD.PSHNUM_0 = PREQUIS.PSHNUM_0
    WHERE (APPFLG_0 = 3 OR APPFLG_0 = 2) 
      AND (AWRKHISSUI.ACTSIG_0 = 'REJ' OR AWRKHISSUI.ACTSIG_0 = 'VAL')
      AND (PREQUISD.YAPP_0 = '' OR PREQUISD.YAPP_0 IS NULL)
    GROUP BY 
        PREQUIS.PSHNUM_0, AWRKHISSUI.DEST_0, AWRKHISSUI.ACTSIG_0,APPFLG_0
    """
    with connection.cursor() as cursor:
        try:
            cursor.execute(query)
            rows = cursor.fetchall()
            result = [
                {
                    "num_piece": r[0],
                    "validateur": r[1],
                    "action": r[2]
                }
                for r in rows
            ]
            return result
        except Exception as e:
            print("Erreur SQL:", str(e))
            raise e

def get_commande_approve_in_x3():
    query = """
    SELECT DISTINCT 
        PORDER.POHNUM_0 AS uid,
        AWRKHISSUI.DEST_0 AS validateur,
        AWRKHISSUI.ACTSIG_0 AS action,
        CASE
            WHEN PORDER.APPFLG_0 = 1 THEN 'NON'
            WHEN PORDER.APPFLG_0 = 2 THEN 'Partiellement'
            WHEN PORDER.APPFLG_0 = 3 THEN 'TOTALEMENT'
        END AS signature
    FROM HABIBO.PORDER
    INNER JOIN HABIBO.AWRKHISSUI ON AWRKHISSUI.CLEOBJ_0 = PORDER.POHNUM_0
    LEFT JOIN HABIBO.AOBJTXT ON PORDER.POHNUM_0 = AOBJTXT.IDENT1_0
    INNER JOIN HABIBO.AUTILIS ON PORDER.BUY_0= AUTILIS.USR_0
    INNER JOIN HABIBO.PORDERQ ON PORDER.POHNUM_0 = PORDERQ.POHNUM_0
    WHERE 
        AWRKHISSUI.DEST_0 <> 'ADMIN' and (PORDER.YAPP_0 = '' OR PORDER.YAPP_0 is null)
    GROUP BY 
        PORDER.POHNUM_0, AWRKHISSUI.DEST_0, AWRKHISSUI.ACTSIG_0,PORDER.APPFLG_0,PORDER.YAPP_0
    """
    with connection.cursor() as cursor:
        try:
            cursor.execute(query)
            rows = cursor.fetchall()
            result = [
                {
                    "num_piece": r[0],
                    "validateur": r[1],
                    "action": r[2],
                    "signature": r[3]
                }
                for r in rows
            ]
            return result
        except Exception as e:
            print("Erreur SQL:", str(e))
            raise e

def get_details_requisition_approve_in_x3():
    query = """
    select DISTINCT 
        PREQUIS.PSHNUM_0 as num_piece,
        PREQUISD.ITMREF_0 AS code_article,
        AWRKHISSUI.DEST_0 as validateur,
        AWRKHISSUI.ACTSIG_0 AS action
    from HABIBO.PREQUISD 
    INNER JOIN HABIBO.AWRKHISSUI on AWRKHISSUI.CLEOBJ_0 = PREQUISD.PSHNUM_0 AND AWRKHISSUI.VALCTX4_0 = PREQUISD.ITMREF_0 
    INNER JOIN HABIBO.PREQUIS on PREQUISD.PSHNUM_0 = PREQUIS.PSHNUM_0
    INNER JOIN HABIBO.ITMMASTER on PREQUISD.ITMREF_0 = ITMMASTER.ITMREF_0
    LEFT JOIN HABIBO.AOBJTXT on PREQUIS.PSHNUM_0 = AOBJTXT.IDENT1_0
    INNER JOIN HABIBO.APLSTD on PREQUISD.YDIRECTION_0 = APLSTD.LANNUM_0 and LANCHP_0 = 6002
    INNER JOIN HABIBO.AUTILIS on PREQUIS.REQUSR_0 = AUTILIS.USR_0
    where  ABROBJ_0 = 'PSH' AND (AWRKHISSUI.ACTSIG_0 = 'REJ' OR AWRKHISSUI.ACTSIG_0 = 'VAL')
    AND (PREQUISD.YAPP_0 ='' or PREQUISD.YAPP_0 = null)
    and PREQUIS.APPFLG_0 <> 3
    GROUP BY 
        PREQUIS.PSHNUM_0,PREQUISD.ITMREF_0,AWRKHISSUI.DEST_0,AWRKHISSUI.ACTSIG_0
    """
    with connection.cursor() as cursor:
        cursor.execute(query)
        rows = cursor.fetchall()
        result = [
            {
                "num_piece": r[0],
                "article": r[1],
                "validateur": r[2],
                "action": r[3]
            }
            for r in rows
        ]
        return result
#---------end----------

#------------requete de validation ou de refus----------
def update_requisition(data):
    uid = data.get('uid')
    valeur1 = int(data.get('valeur1', 3))
    valeur2 = int(data.get('valeur2', 3))
    valeur3 = int(data.get('valeur3', 5))
    id_x3 = data.get('id_x3', 'ADMIN')
    designation = data.get('designation', 'VAL')

    if not uid:
        raise ValueError("Parameter 'uid' is required")

    queries = [
        """UPDATE HABIBO.PREQUIS SET APPFLG_0 = %s WHERE PSHNUM_0 = %s""",
        """UPDATE HABIBO.PREQUISD SET LINAPPFLG_0 = %s,YAPP_0='OUI' WHERE PSHNUM_0 = %s AND LINAPPFLG_0 =1""",
        """UPDATE HABIBO.AWRKHISSUI 
           SET FLGSIG_0 = %s, ACTSIG_0 = %s, USRSIG_0 = %s,
               DATSIG_0 = CONVERT(VARCHAR(6), GETDATE(), 12),
               TIMSIG_0 = REPLACE(CONVERT(VARCHAR(8), SYSDATETIME(), 108), ':', '')
           WHERE CODEVT_0 = 'PSH' AND FLGSIG_0 = 3 AND CLEOBJ_0 = %s"""
    ]

    params_list = [
        (valeur1, uid),
        (valeur2, uid),
        (valeur3, designation, id_x3, uid)
    ]

    with connection.cursor() as cursor:
        for query, params in zip(queries, params_list):
            cursor.execute(query, params)

    return {
        "status": "success",
        "message": "Update completed successfully",
        "used_params": {
            "uid": uid,
            "APPFLG_0": valeur1,
            "LINAPPFLG_0": valeur2,
            "FLGSIG_0": valeur3,
            "USRSIG_0": id_x3,
            "ACTSIG_0": designation
        }
    }

def update_commande(data):
    uid = data.get('uid')
    valeur1 = int(data.get('valeur1', 3))
    id_x3 = data.get('id_x3', 'ADMIN')
    designation = data.get('designation', 'VAL')

    if not uid:
        raise ValueError("Parameter 'uid' is required")

    queries = [
        """update HABIBO.PORDER set APPFLG_0=%s,YAPP_0='OUI' where POHNUM_0 =%s""",
        """update HABIBO.AWRKHISSUI set FLGSIG_0=5, ACTSIG_0 = %s, 
            USRSIG_0 = %s,DATSIG_0 = CONVERT(VARCHAR(6), GETDATE(), 12),
            TIMSIG_0 = REPLACE(CONVERT(VARCHAR(8), SYSDATETIME(), 108), ':', '') 
            where CODEVT_0 = 'POH' and FLGSIG_0 = 3 
            and CLEDEC_0 = %s"""
    ]

    params_list = [
        (valeur1, uid),
        (designation, id_x3, uid)
    ]

    with connection.cursor() as cursor:
        for query, params in zip(queries, params_list):
            cursor.execute(query, params)

    return {
        "status": "success",
        "message": "Update completed successfully",
        "used_params": {
            "uid": uid,
            "APPFLG_0": valeur1,
            "USRSIG_0": id_x3,
            "ACTSIG_0": designation
        }
    }

def update_facture(data):
    valeur1 = int(data.get('valeur1', 3))
    uid = data.get('uid')

    if not uid:
        raise ValueError("Parameter 'uid' is required")

    queries = [
        """update HABIBO.PINVOICE set PAZ_0 =%s where NUM_0 =%s"""
    ]

    params_list = [
        (valeur1, uid)
    ]

    with connection.cursor() as cursor:
        for query, params in zip(queries, params_list):
            cursor.execute(query, params)

    return {
        "status": "success",
        "message": "Update completed successfully",
        "used_params": {
            "uid": uid,
            "APPFLG_0": valeur1
        }
    }

def update_article(data):
    uid = data.get('uid')
    code_article = data.get('code_article')
    valeur1 = int(data.get('valeur1', 3))
    valeur2 = int(data.get('valeur2', 3))
    valeur3 = int(data.get('valeur3', 5))
    id_x3 = data.get('id_x3', 'ADMIN')
    designation = data.get('designation', 'VAL')

    if not uid:
        raise ValueError("Parameter 'uid' is required")

    queries = [
        """UPDATE HABIBO.PREQUIS SET APPFLG_0 = %s WHERE PSHNUM_0 = %s""",
        """UPDATE HABIBO.PREQUISD SET LINAPPFLG_0 = %s,YAPP_0='OUI' WHERE PSHNUM_0 = %s and ITMREF_0 =%s""",
        """UPDATE HABIBO.AWRKHISSUI 
               SET FLGSIG_0 = %s,
                   ACTSIG_0 = %s,
                   USRSIG_0 = %s,
                   DATSIG_0 = CONVERT(VARCHAR(6), GETDATE(), 12),
                   TIMSIG_0 = REPLACE(CONVERT(VARCHAR(8), SYSDATETIME(), 108), ':', '')
               WHERE CODEVT_0 = 'PSH' 
               AND FLGSIG_0 = 3 
               AND VALCTX4_0 = %s
               AND CLEOBJ_0 = %s"""
    ]

    params_list = [
        (valeur1, uid),
        (valeur2, uid,code_article),
        (valeur3, designation, id_x3,code_article,uid)
    ]

    with connection.cursor() as cursor:
        for query, params in zip(queries, params_list):
            cursor.execute(query, params)

    return {
        "status": "success",
        "message": "Update completed successfully",
        "used_params": {
            "uid": uid,
            "article": code_article,
            "APPFLG_0": valeur1,
            "LINAPPFLG_0": valeur2,
            "FLGSIG_0": valeur3,
            "USRSIG_0": id_x3,
            "ACTSIG_0": designation
        }
    }


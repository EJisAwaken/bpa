CREATE DATABASE IF NOT EXISTS validation_bpa;
USE validation_bpa

create table role (
    id_role int auto_increment primary key,
    unique_role varchar(200) unique,
    role varchar(200),
    active boolean default true
);

create table user (
    id_user int auto_increment primary key,
    nom text,
    prenom text,
    email varchar(225),
    id_role int references role(id_role),
    id_x3 VARCHAR(200) unique,
    interim VARCHAR(200),
    mot_de_passe varchar(200),
    direction varchar(100),
    active boolean default true
);

create table duree(
    id_duree int auto_increment primary key,
    id_user int references user(id_user),
    debut datetime,
    fin datetime,
    active boolean default true
);

create table etat(
    id_etat int auto_increment primary key,
    value int unique,
    def text
);

create table menu(
    id_menu int auto_increment primary key,
    role varchar(200),
    label varchar(400),
    icone varchar(400),
    route varchar(400),
    active boolean default true
);

insert menu(role,label,icone,route) values ("admin","Dashboard","fas fa-fw fa-chart-bar","/dash-admin"),("admin","Gestion d'utilisateur","fas fa-fw fa-user","/users");
insert menu(role,label,icone,route) values ("admin","Gestion des rôles","fas fa-fw fa-cog","/roles"),("admin","Historique de validation","fas fa-fw fa-list","/history");
insert menu(role,label,icone,route) values ("admin","Liste des DA","fas fa-fw fa-list","/lec-da"),("admin","Liste des BC","fas fa-fw fa-list","/lec-bc");
insert menu(role,label,icone,route) values ("val","Dashboard","fas fa-fw fa-chart-bar","/dash-val"),("val","Liste des demandes d'achats","fas fa-fw fa-list","/list-da");
insert menu(role,label,icone,route) values ("val","Liste des bons de commande","fas fa-fw fa-list","/list-bc"),("val","Historique de validation","fas fa-fw fa-list","/historyForUser");
insert menu(role,label,icone,route) values ("valspl","Dashboard","fas fa-fw fa-chart-bar","/dash-val"),("valspl","Liste des demandes d'achats","fas fa-fw fa-list","/list-da");
insert menu(role,label,icone,route) values ("valspl","Liste des bons de commande","fas fa-fw fa-list","/list-bc"),("valspl","Historique de validation","fas fa-fw fa-list","/historyForUser");
insert menu(role,label,icone,route) values ("valspl","Liste des factures","fas fa-fw fa-list","/facture");

CREATE TABLE demande(
    id_demande INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200),
    societe VARCHAR(200) ,
    demandeur text,
    email text,
    beneficiaire text,
    id_x3 VARCHAR(200),
    validateur VARCHAR(200),
    date VARCHAR(200) ,
    montant text,
    signature VARCHAR(100),
    codeA VARCHAR(100),
    lien_pj text,
    etat INT,
    isnotif boolean default false,
    FOREIGN KEY (etat) REFERENCES etat(id_etat)
);

create table detail(
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200) UNIQUE,
    ref_dem VARCHAR(200),
    code_article VARCHAR(200),
    quantite VARCHAR(200),
    pu VARCHAR(200  ),
    objet VARCHAR(200),
    montant text,
    devise VARCHAR(200),
    etat INT,
    id_x3 VARCHAR(200),
    FOREIGN KEY (etat) REFERENCES etat(id_etat)
);

CREATE TABLE commande(
    id_commande INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200),
    societe VARCHAR(200) ,
    demandeur text,
    email text,
    beneficiaire VARCHAR(200),
    id_x3 VARCHAR(200),
    validateur VARCHAR(200),
    date VARCHAR(200) ,
    montant text,
    devise VARCHAR(100),
    codepayement VARCHAR(100),
    modepayement VARCHAR(100),
    codeA VARCHAR(100),
    signature VARCHAR(100),
    lien_pj text,
    etat INT,
    isnotif boolean default false,
    FOREIGN KEY (etat) REFERENCES etat(id_etat)
);

create table if not EXISTS detailbc(
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200) UNIQUE,
    ref_dem VARCHAR(200),
    code_article VARCHAR(200),
    quantite VARCHAR(200),
    pu VARCHAR(200  ),
    objet VARCHAR(200),
    devise VARCHAR(200),
    montant text
);

create table if not EXISTS facture(
    id_facture INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200) UNIQUE,
    societe VARCHAR(200),
    fournisseur VARCHAR(200),
    date VARCHAR(200),
    montant text,
    devise VARCHAR(100)
);

create table if not EXISTS detailmere(
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200) UNIQUE,
    mere VARCHAR(200),
    code_article VARCHAR(200),
    societe VARCHAR(200),
    fournisseur VARCHAR(200),
    objet VARCHAR(200),
    quantite VARCHAR(200),
    pu VARCHAR(200),
    devise VARCHAR(200),
    montant text
);

create table if not EXISTS facturecomp(
    id_facturecomp INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200),
    reffc VARCHAR(200),
    mere VARCHAR(200),
    societe VARCHAR(200),
    fournisseur VARCHAR(200),
    date VARCHAR(200),
    code VARCHAR(200),
    objet VARCHAR(200),
    montant text,
    devise VARCHAR(100),
    etat int
);

create table if not EXISTS facturecompett(
    id_facturecompett INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200),
    societe VARCHAR(200),
    fournisseur VARCHAR(200),
    date VARCHAR(200),
    montant text,
    devise VARCHAR(100),
    isnotif boolean default false,
    etat INT
);

create table validation(
    id_validation int auto_increment primary key,
    demande varchar(100),
    objet varchar(100),
    montant text,
    id_etat int references etat(id_etat),
    id_user int references user(id_user),
    motif text,
    is_da int default 0,
    date_validation datetime default CURRENT_TIMESTAMP,
    active boolean default true
);

create table notification(
    id_notification int auto_increment primary key,
    message text,
    id_x3 varchar(100),
    etat int,
    uid varchar(100),
    recus boolean default false,
    lus boolean default false,
    date_notification datetime default CURRENT_TIMESTAMP
);

create table finaldestination(
    id_final int auto_increment primary key,
    uid varchar(100),
    designation varchar(300)
);

insert into role(id_role,unique_role,role) values (1,"admin","administrateur"),(2,"val","Validateur");
insert into role(id_role,unique_role,role) values (3,"valspl","validateur spéciale"),(4,"lec","lecteur");

insert into etat(id_etat,value,def) values (1,1,"En attent de validation"),(2,2,"validé"),(3,3,"refusé"),(4,4,"Partiellement");
-- insert into etat(id_etat,value,def) values (5,4,"Partiellement");
-- insert into etat(id_etat,value,def) values (6,5,"En attent"),(7,6,"Bon à payer"),(8,7,"Avoir"),(9,8,"Litige");

COMMIT;

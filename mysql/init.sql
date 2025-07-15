create database IF NOT EXISTS validation_bpa;
USE validation_bpa

create table IF NOT EXISTS role (
    id_role int auto_increment primary key,
    unique_role varchar(200) unique,
    role varchar(200),
    active boolean default true
);

insert into role(unique_role,role) values ("admin","administrateur"),("val","Validateur");
insert into role(unique_role,role) values ("valdef","validateur definitive");

create table IF NOT EXISTS user (
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

create table IF NOT EXISTS duree(
    id_duree int auto_increment primary key,
    id_user int references user(id_user),
    debut datetime,
    fin datetime,
    active boolean default true
);

create table IF NOT EXISTS etat(
    id_etat int auto_increment primary key,
    value int unique,
    def text
);

CREATE TABLE IF NOT EXISTS demande(
    id_demande INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200),
    societe VARCHAR(200) ,
    demandeur text,
    email text,
    id_x3 VARCHAR(200),
    date DATE ,
    date_expiration DATE,
    montant text,
    signature VARCHAR(100),
    lien_pj text,
    etat INT,
    isnotif boolean default false,
    FOREIGN KEY (etat) REFERENCES etat(id_etat)
);

create table IF NOT EXISTS detail(
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200) UNIQUE,
    ref_dem VARCHAR(200),
    code_article VARCHAR(200),
    quantite VARCHAR(200),
    pu VARCHAR(200  ),
    objet VARCHAR(200),
    montant text,
    etat INT,
    id_x3 VARCHAR(200),
    FOREIGN KEY (etat) REFERENCES etat(id_etat)
);

CREATE TABLE IF NOT EXISTS commande(
    id_commande INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(200),
    societe VARCHAR(200) ,
    demandeur text,
    email text,
    id_x3 VARCHAR(200),
    date DATE ,
    date_expiration DATE,
    montant text,
    signature VARCHAR(100),
    lien_pj text,
    etat INT,
    isnotif boolean default false,
    FOREIGN KEY (etat) REFERENCES etat(id_etat)
);

insert into etat(value,def) values (0,"supprimée"),(1,"créer"),(2,"validé"),(3,"refusé");
insert into etat(value,def) values (4,"Partiellement");

create table IF NOT EXISTS validation(
    id_validation int auto_increment primary key,
    demande varchar(100),
    id_etat int references etat(id_etat),
    id_user int references user(id_user),
    motif text,
    is_exported boolean default false,
    date_validation datetime default CURRENT_TIMESTAMP,
    active boolean default true
);

create table IF NOT EXISTS notification(
    id_notification int auto_increment primary key,
    message text,
    id_x3 varchar(100),
    etat int,
    uid varchar(100),
    recus boolean default false,
    lus boolean default false,
    date_notification datetime default CURRENT_TIMESTAMP
);
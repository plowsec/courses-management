drop table if exists Implique;
drop table if exists Inscription;
drop table if exists PeriodeInscription;
drop table if exists Cours;
drop table if exists Administrator;
drop table if exists Professeur;
drop table if exists Eleve;

create table Administrator(
    nom char(20) not null,
    prenom char(20) not null,
    nomUtilisateur varchar(20) not null,
    motDePasse  VARCHAR(80) not null,
    adresse char(200) not null,
    primary key (nomUtilisateur)
);

create table Eleve(
    nom char(20) not null,
    prenom char(20) not null,
    nomUtilisateur varchar(20) not null,
    motDePasse  VARCHAR(80) not null,
    adresse char(200) not null,
    primary key (nomUtilisateur)
);

create table Professeur(
    nom char(20) not null,
    estChefFiliere boolean not null,
    prenom char(20) not null,
    nomUtilisateur varchar(20) not null,
    motDePasse  VARCHAR(80) not null,
    adresse char(200) not null,
    primary key (nomUtilisateur)
);

create table PeriodeInscription(
    dateDebut date not null,
    dateFin date not null,
    primary key (dateDebut, dateFin),
    constraint dt_fin check(dateDebut < dateFin) 
);

create table Cours(
    coursId integer auto_increment not null,
    titre char(40) not null,
    semestrePref char(50) not null,
    descriptionXML text not null,
    etudMax integer,
    etat char(20) not null default 'Brouillon',
    primary key(coursId),
    constraint etat_val check(etat in('Brouillon', 'Valide', 'Actif'))
);

create table Implique(
    fkProf char(20),
    fkCours integer,
    foreign key(fkProf) references Professeur(nomUtilisateur),
    foreign key(fkCours) references Cours(coursId),
    constraint impl_unique unique(fkProf, fkCours),
    primary key(fkProf, fkCours)
);

create table Inscription(
    fkEleve char(20) not null,
    fkCours integer not null,
    priorite integer,
    foreign key(fkEleve) references Eleve(nomUtilisateur),
    foreign key(fkCours) references Cours(coursId),
    constraint pr_value check(priorite >0 and priorite < 3),
    constraint inscr_unique unique(fkEleve, fkCours),
    primary key(fkEleve, fkCours)
);


/* --------------------------------------------------------- */
/* --------------------- Administrator --------------------- */
/* --------------------------------------------------------- */


/*mdp = admin */
insert into Administrator(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Nguyen", "John", "john.nguyen", "$2a$10$O3zA7CCFie6WQ8ag9XRzL.jxju2JCBuj8LWJ7N2Y1hbvBRfm5CaSy",'<adresse><rue num="69">Imp des endives</rue><ville npa="1700">Fribourg</ville></adresse>');


/* --------------------------------------------------------- */
/* ------------------------- ELEVE -------------------------
/* --------------------------------------------------------- */


/*mdp = vmotdepasse */
insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Meier","John", "john.meier", '$2a$10$C2JxB3yjV.AxePVy6.z8letnYmkj79gZk.lqLk5D9FxQ./HCnGBaW','<adresse><rue num="69">Imp des patates</rue><ville npa="1700">Fribourg</ville></adresse>');

/*mdp = bmotdepasse */
insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Doe","Bastien", "bastien.doe", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("McDonalds","Lucas", "lucas.mcdonalds", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("McBrownie","Ricardo", "ricardo.mcbrownie", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Kron","Thomas", "thomas.kron", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Wonders","Karim", "karim.wonders", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Granger","Alexandre", "alexandr.granger", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Wolmey","Timothée", "timothee.wolmey", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des carottes</rue><ville npa=\"1700\">Fribourg</ville></adresse>");


/* --------------------------------------------------------- */
/* ---------------------- Professeur ----------------------- */
/* --------------------------------------------------------- */


insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Kartner", 1, "Philippe", "philippe.kartner", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des choux-fleurs</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Kovinsky", 0, "Pierre", "pierre.kovinsky", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des choux-fleurs</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Caon", 0, "Paolo", "paolo.caon", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("LeBlanc", 0, "Houda", "houda.leblanc", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Homard", 0, "Omar", "omar.homard", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Kartner", 0, "Elena", "elena.kartner", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Medici", 0, "Sandy", "sandy.medici", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Stuhler", 0, "Jean-Roland", "jean-rol.stuhler", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Minolta", 0, "Francois", "francois.minolta", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");

insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values
("Konica", 0, "Richard", "richard.konica", "$2a$10$mwIk/ukWrgAD6Xbf1dxkZ.V59B4ST4koMpPzgERXA3DJBO5NVKz92","<adresse><rue num=\"69\">Imp des petits pois</rue><ville npa=\"1700\">Fribourg</ville></adresse>");


/* --------------------------------------------------------- */
/* ------------------- PeriodeInscription ------------------ */
/* --------------------------------------------------------- */


insert into PeriodeInscription(dateDebut, dateFin) values
("2019-1-25", "2030-5-30");


/* --------------------------------------------------------- */
/* ------------------------- Cours ------------------------- */
/* --------------------------------------------------------- */


/*drafts*/
insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Deep Learning", "Printemps", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus.", 30, "Brouillon");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Ordinateurs quantiques", "Automne", "Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi.", 21, "Brouillon");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Programmation fonctionnelle", "Automne", "Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat.", 21, "Brouillon");


/*valides*/
insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
('Simulation numérique', "Printemps", "Duis semper. Duis arcu massa, scelerisque vitae, consequat in, pretium a, enim. Pellentesque congue. Ut in risus volutpat libero pharetra tempor. Cras vestibulum bibendum augue.", 2, "Valide");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
('Algorithmes de suggestion', "Automne", "Praesent blandit odio eu enim.", 30, "Valide");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
('Éthique', "Automne", "Pellentesque sed dui ut augue blandit sodales.", 30, "Valide");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
('Innovation', "Automne", "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam nibh.", 30, "Valide");


/*actifs*/
insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("NoSQL", "Automne", "Mauris ac mauris sed pede pellentesque fermentum.", 30, "Actif");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Advanced interfaces", "Automne", "Maecenas adipiscing ante non diam sodales hendrerit.", 30, "Actif");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Machine Learning", "Printemps", "Ut velit mauris, egestas sed, gravida nec, ornare ut, mi. Aenean ut orci vel massa suscipit pulvinar.", 30, "Actif");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Sécurité IT", "Automne", "Nulla sollicitudin. Fusce varius, ligula non tempus aliquam, nunc turpis ullamcorper nibh, in tempus sapien eros vitae ligula.", 30, "Actif");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
("Game Design", "Automne", "Pellentesque rhoncus nunc et augue. Integer id felis. Curabitur aliquet pellentesque diam. Integer quis metus vitae elit lobortis egestas.", 30, "Actif");

insert into Cours(titre, semestrePref, descriptionXML, etudMax, etat) values
('Traitement d\'images', "Automne", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi vel erat non mauris convallis vehicula.", 30, "Valide");


/* --------------------------------------------------------- */
/* ----------------------- Implique ------------------------ */
/* --------------------------------------------------------- */


insert into Implique(fkProf, fkCours) values 
("pierre.kovinsky", 1);

insert into Implique(fkProf, fkCours) values 
("pierre.kovinsky", 2);

insert into Implique(fkProf, fkCours) values 
("jean-rol.stuhler", 3);

insert into Implique(fkProf, fkCours) values 
("francois.minolta", 3);

insert into Implique(fkProf, fkCours) values 
("richard.konica", 4);

insert into Implique(fkProf, fkCours) values 
("francois.minolta", 5);

insert into Implique(fkProf, fkCours) values 
("francois.minolta", 6);

insert into Implique(fkProf, fkCours) values 
("houda.leblanc", 7);

insert into Implique(fkProf, fkCours) values 
("omar.homard", 8);

insert into Implique(fkProf, fkCours) values 
("jean-rol.stuhler", 8);

insert into Implique(fkProf, fkCours) values 
("elena.kartner", 8);

insert into Implique(fkProf, fkCours) values 
("omar.homard", 9);

insert into Implique(fkProf, fkCours) values 
("elena.kartner", 9);

insert into Implique(fkProf, fkCours) values 
("jean-rol.stuhler", 10);

insert into Implique(fkProf, fkCours) values 
("houda.leblanc", 11);

insert into Implique(fkProf, fkCours) values 
("jean-rol.stuhler", 12);

insert into Implique(fkProf, fkCours) values 
("omar.homard", 12);

insert into Implique(fkProf, fkCours) values 
("sandy.medici", 13);


/* --------------------------------------------------------- */
/* ---------------------- Inscription ---------------------- */
/* --------------------------------------------------------- */

insert into Inscription(fkEleve, fkCours, priorite) values
("alexandr.granger", 1, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("john.meier", 1, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("john.meier", 2, 2);

insert into Inscription(fkEleve, fkCours, priorite) values
("lucas.mcdonalds", 2, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("alexandr.granger", 2, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("alexandr.granger", 3, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("ricardo.mcbrownie", 3, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("john.meier", 4, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("karim.wonders", 4, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("karim.wonders", 5, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("lucas.mcdonalds", 6, 2);

insert into Inscription(fkEleve, fkCours, priorite) values
("lucas.mcdonalds", 7, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("ricardo.mcbrownie", 8, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("alexandr.granger", 8, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("karim.wonders", 8, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("john.meier", 8, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("lucas.mcdonalds", 8, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("ricardo.mcbrownie", 9, 2);

insert into Inscription(fkEleve, fkCours, priorite) values
("karim.wonders", 9, 2);

insert into Inscription(fkEleve, fkCours, priorite) values
("john.meier", 9, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("ricardo.mcbrownie", 10, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("alexandr.granger", 10, 2);

insert into Inscription(fkEleve, fkCours, priorite) values
("ricardo.mcbrownie", 11, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("karim.wonders", 12, 1);

insert into Inscription(fkEleve, fkCours, priorite) values
("lucas.mcdonalds", 13, 1);

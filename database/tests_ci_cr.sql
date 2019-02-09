/* test c2 */
insert into Professeur(nom, estChefFiliere, prenom, nomUtilisateur, motDePasse, adresse) values ("Joye", 1, "Philippe", "philippe.joye", "test","test");

/* test c3 */
update PeriodeInscription set dateDebut = "2018-12-12" where dateFin = "2018-5-30";

/* test c4 */
insert into PeriodeInscription(dateDebut, dateFin) values ("2018-6-01", "2018-7-30");

/* test c5 : etudMax is 1 in Cours.fkCours = 4 */
/* first one should pass*/
insert into Inscription(fkEleve, fkCours, priorite) values
("bastien.monney", 4, 2);

insert into Inscription(fkEleve, fkCours, priorite) values
("thomas.schaller", 4, 2);

update Inscription set fkCours = 4 where fkEleve = 'karim.romanens';


/* test cr1 */
insert into Inscription(fkEleve, fkCours, priorite) values
("bastien.monney", 1, 2);

/* test cr2 : vladimir.nguyen already exists in Admin*/
insert into Eleve(nom, prenom, nomUtilisateur, motDePasse, adresse) values
("Nguyen", "Vladimir", "vladimir.nguyen", "$2a$10$O3zA7CCFie6WQ8ag9XRzL.jxju2JCBuj8LWJ7N2Y1hbvBRfm5CaSy",'<adresse><rue num="69">Imp des endives</rue><ville npa="1700">Fribourg</ville></adresse>');

update Eleve set nomUtilisateur = 'vladimir.meier' where nomUtilisateur = 'bastien.monney';



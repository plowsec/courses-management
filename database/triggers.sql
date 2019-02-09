
/*
    c2 : 1 seul chef de filière
    cr2 : nomUtilisateur unique à travers les tables
*/

drop trigger if exists headteacher_unique;
delimiter //
create trigger headteacher_unique before insert
on Professeur for each row
begin
    declare m_nb_headteachers integer;
    set @m_nb_headteachers := (select count(*) from Professeur where estChefFiliere=TRUE);
    
    if new.estChefFiliere = true
    then
        if m_nb_headteachers > 0
        then
            SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Only 1 headteacher allowed';
        end if;
    end if;
    
        
   if (select count(1) from Professeur where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Eleve where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Administrator where nomUtilisateur = new.nomUtilisateur) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Username already in use';
    end if;
end//
delimiter ;

drop trigger if exists headteacher_unique_update;
delimiter //
create trigger headteacher_unique_update before update
on Professeur for each row
begin
    declare m_nb_headteachers integer;
    set @m_nb_headteachers := (select count(*) from Professeur where estChefFiliere=TRUE);
    
    if new.estChefFiliere = true
    then
        if m_nb_headteachers > 0
        then
            SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Only 1 headteacher allowed';
        end if;
    end if;
    
        
   if (select count(1) from Professeur where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Eleve where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Administrator where nomUtilisateur = new.nomUtilisateur) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Username already in use';
    end if;
end//
delimiter ;

/*
    
    --if new.dateDebut >= new.dateFin
    --then
    --    SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'dateDebut >= dateFin';
    --end if;
    c4 : 1 seule période d'inscription
*/
drop trigger if exists period_unique;
delimiter //
create trigger period_unique before insert
on PeriodeInscription for each row
begin
    if (select count(1) from PeriodeInscription) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Only 1 registration period allowed';
    end if;
    
    if new.dateDebut >= new.dateFin
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'dateDebut >= dateFin';
    end if;

end//
delimiter ;

/*
    c3 : dateDebut doit être < dateFin
*/
drop trigger if exists period_valide;
delimiter //
create trigger period_valide before update
on PeriodeInscription for each row
begin
    
    if new.dateDebut >= new.dateFin
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'dateDebut >= dateFin';
    end if;

end//
delimiter ;

/*
    cr2 : nomUtilisateur unique à travers les tables
*/
drop trigger if exists username_valide_insert_eleve;
delimiter //
create trigger username_valide_insert_eleve before insert
on Eleve for each row
begin
    
    if (select count(1) from Professeur where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Eleve where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Administrator where nomUtilisateur = new.nomUtilisateur) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Username already in use';
    end if;

end//
delimiter ;

drop trigger if exists username_valide_update_eleve;
delimiter //
create trigger username_valide_update_eleve before update
on Eleve for each row
begin
    
    if (select count(1) from Professeur where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Eleve where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Administrator where nomUtilisateur = new.nomUtilisateur) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Username already in use';
    end if;

end//
delimiter ;

drop trigger if exists username_valide_insert_admin;
delimiter //
create trigger username_valide_insert_admin before insert
on Administrator for each row
begin
    
   if (select count(1) from Professeur where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Eleve where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Administrator where nomUtilisateur = new.nomUtilisateur) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Username already in use';
    end if;

end//
delimiter ;

drop trigger if exists username_valide_update_admin;
delimiter //
create trigger username_valide_update_admin before update
on Administrator for each row
begin
    
   if (select count(1) from Professeur where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Eleve where nomUtilisateur = new.nomUtilisateur) > 0
    or (select count(1) from Administrator where nomUtilisateur = new.nomUtilisateur) > 0
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Username already in use';
    end if;

end//
delimiter ;

/* 
    c5 : entre 0 et maxInsc inscriptions à un cours si maxInsc existe
    cr1 : un étudiant doit s'inscrire à des cours ayant l'état "Valide"
*/
drop trigger if exists registrations_valide_insert;
delimiter //
create trigger registrations_valide_insert before insert
on Inscription for each row
begin
    declare m_nb_regs integer;
    set m_nb_regs := (select etudMax from Cours where coursId = new.fkCours);
    
    if (m_nb_regs) is not null
       then
        if (select count(*) from Inscription where fkCours = new.fkCours) >= m_nb_regs
        then
            SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Maximum number of registrations reached';
        end if;
    end if;
    
    if (select etat from Cours where coursId = new.fkCours) <> 'Valide'
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Students can only register themselves to courses with valid state';
    end if;

end//
delimiter ;

drop trigger if exists registrations_valide_update;
delimiter //
create trigger registrations_valide_update before update
on Inscription for each row
begin
    declare m_nb_regs integer;
    set m_nb_regs := (select etudMax from Cours where coursId = new.fkCours);

    if (m_nb_regs) is not null
    then
        if (select count(*) from Inscription where fkCours = new.fkCours) >= m_nb_regs
        then
            SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Maximum number of registrations reached';
        end if;
    end if;
    
    if (select etat from Cours where coursId = new.fkCours) <> 'Valide'
    then
        SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Students can only register themselves to courses with valid state';
    end if;
    
end//
delimiter ;


/* c6 : cours non activés repassent à l'état brouillon et leurs inscriptions sont nettoyées */

drop procedure if exists clean_non_activated_courses;
delimiter //
create procedure clean_non_activated_courses()
begin
    delete inscr from Inscription as inscr join Cours on inscr.fkCours = Cours.coursId where Cours.etat ='Valide';
    update Cours set etat = 'Brouillon' where etat = 'Valide';

end//
delimiter ;


/* c1 : 4 inscription de priorité 1 et 1 inscription de priorité 2 */

drop procedure if exists check_registrations_valides;
delimiter //
create procedure check_registrations_valides()
begin
    select count(*) from Inscription join Cours on Inscription.fkCours = Cours.coursId where Inscription.fkEleve='vladimir.meier' and priorite = 1;
    select count(*) from Inscription join Cours on Inscription.fkCours = Cours.coursId where Inscription.fkEleve='vladimir.meier' and priorite = 2;
end//
delimiter ;





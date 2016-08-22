--                     ORGANIZATION INSERTIONS                     --
INSERT INTO `organization`(name, number_of_employees) VALUES ('Google', 0);
INSERT INTO `organization`(name, number_of_employees) VALUES ('Monitise', 0);
INSERT INTO `organization`(name, number_of_employees) VALUES ('Pozitron', 0);


--                       JOB TITLE INSERTIONS                      --
INSERT INTO `job_title`(title, organization_id) VALUES('android dev',1);
INSERT INTO `job_title`(title, organization_id) VALUES('ios dev',1);
INSERT INTO `job_title`(title, organization_id) VALUES('ios dev',2);
INSERT INTO `job_title`(title, organization_id) VALUES('janitor',2);
INSERT INTO `job_title`(title, organization_id) VALUES('back end',2);


--                ORGANIZATION-JOB TITLE INSERTIONS                --
INSERT INTO `organization_job_titles`(organization_id, job_titles_id) VALUES(1, 1);
INSERT INTO `organization_job_titles`(organization_id, job_titles_id) VALUES(1, 2);
INSERT INTO `organization_job_titles`(organization_id, job_titles_id) VALUES(2, 3);
INSERT INTO `organization_job_titles`(organization_id, job_titles_id) VALUES(2, 4);
INSERT INTO `organization_job_titles`(organization_id, job_titles_id) VALUES(2, 5);
-- google jobs: android dev, ios dev and janitor.
-- monitise jobs: ios dev, janitor and back end.


--                         USER INSERTIONS                         --
-- google users
INSERT INTO `user`(name, password, role, surname, username, organization_id)
    VALUES('Google', '123', 'MANAGER', 'Manager', 'google.manager', 1);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Pelin', '123', 'TEAM_LEADER', 'Sonmez', 'pelin.sonmez', 1, 1);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Faruk', '123', 'EMPLOYEE', 'Gulmez', 'faruk.gulmez', 2, 1);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Pelya', '123', 'EMPLOYEE', 'Petroffski', 'pelya.petroffski', 2, 1);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Fatih', '123', 'EMPLOYEE', 'Songul', 'fatih.songul', 1, 1);

-- monitise users
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Monitise', '123', 'MANAGER', 'Manager', 'monitise.manager', 3, 2);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Bilge', '123', 'EMPLOYEE', 'Olmez', 'bilge.olmez', 4, 2);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Yigit', '123', 'EMPLOYEE', 'Gurdal', 'yigit.gurdal', 5, 2);


--                  ORGANIZATION-USER INSERTIONS                   --
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 1);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 2);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 3);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 4);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 5);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 6);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 7);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 8);


--                         TEAM INSERTIONS                         --
INSERT INTO `team`(name, leader_id, organization_id) VALUES('TeamPelin', 2, 1);
INSERT INTO `team`(name, organization_id) VALUES('MonitiseLeaderless', 2);
INSERT INTO `team`(name, organization_id) VALUES('GoogleLeaderless', 1);


--                 ORGANIZATION-TEAMS INSERTIONS                   --
INSERT INTO `organization_teams`(organization_id, teams_id) VALUES(1,1);
INSERT INTO `organization_teams`(organization_id, teams_id) VALUES(2,2);
INSERT INTO `organization_teams`(organization_id, teams_id) VALUES(1,3);


--                      TEAM-MEMBER INSERTIONS                     --
INSERT INTO `team_members`(team_id, members_id) VALUES(1, 2);
INSERT INTO `team_members`(team_id, members_id) VALUES(1, 3);
INSERT INTO `team_members`(team_id, members_id) VALUES(1, 4);
INSERT INTO `team_members`(team_id, members_id) VALUES(2, 7);
INSERT INTO `team_members`(team_id, members_id) VALUES(2, 8);


-- SETTING TEAMS
UPDATE `user` SET team_id = 1 WHERE id = 2;
UPDATE `user` SET team_id = 1 WHERE id = 3;
UPDATE `user` SET team_id = 1 WHERE id = 4;
UPDATE `user` SET team_id = 2 WHERE id = 7;
UPDATE `user` SET team_id = 2 WHERE id = 8;


-- SETTING MANAGERS && NUMBER OF EMPLOYEES
UPDATE `organization` SET manager_id = 1, number_of_employees = 5 WHERE id = 1;
UPDATE `organization` SET manager_id = 6, number_of_employees = 3 WHERE id = 2;


--                       CRITERIA INSERTIONS                       --
INSERT INTO `criteria`(criteria, organization_id) VALUES('Manners', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Code Review', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Code Coverage', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Dress Code', 1);


--              ORGANIZATION-CRITERIA LIST INSERTIONS              --
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,1);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,2);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,3);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,4);


--                  USER-CRITERIA LIST INSERTIONS                  --
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(2,1);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(2,3);

INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(3,1);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(3,2);

INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,1);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,2);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,3);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,4);
--                     ORGANIZATION INSERTIONS                     --
INSERT INTO `organization`(name, number_of_employees) VALUES ('Google', 0);
INSERT INTO `organization`(name, number_of_employees) VALUES ('Monitise', 0);


--                       JOB TITLE INSERTIONS                      --
INSERT INTO `job_title`(title) VALUES('android dev');
INSERT INTO `job_title`(title) VALUES('ios dev');
INSERT INTO `job_title`(title) VALUES('ios dev');
INSERT INTO `job_title`(title) VALUES('janitor');
INSERT INTO `job_title`(title) VALUES('back end');


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
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Google', '123', 'MANAGER', 'Manager', 'google.manager', 1, 1);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Pelin', '123', 'EMPLOYEE', 'Sonmez', 'pelin.sonmez', 1, 1);
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Faruk', '123', 'EMPLOYEE', 'Gulmez', 'faruk.gulmez', 2, 1);
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
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 4);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 5);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 6);


--                         TEAM INSERTIONS                         --
INSERT INTO `team`(name, leader_id, organization_id) VALUES('TeamPelin', 2, 1);
INSERT INTO `team`(name, organization_id) VALUES('Leaderless', 2);


--                 ORGANIZATION-TEAMS INSERTIONS                   --
INSERT INTO `organization_teams`(organization_id, teams_id) VALUES(1,1);
INSERT INTO `organization_teams`(organization_id, teams_id) VALUES(2,2);


--                      TEAM-MEMBER INSERTIONS                     --
INSERT INTO `team_members`(team_id, members_id) VALUES(1, 2);
INSERT INTO `team_members`(team_id, members_id) VALUES(1, 3);
INSERT INTO `team_members`(team_id, members_id) VALUES(2, 5);
INSERT INTO `team_members`(team_id, members_id) VALUES(2, 6);


-- SETTING TEAMS
UPDATE `user` SET team_id = 1 WHERE id = 2;
UPDATE `user` SET team_id = 1 WHERE id = 3;
UPDATE `user` SET team_id = 2 WHERE id = 5;
UPDATE `user` SET team_id = 2 WHERE id = 6;


-- SETTING MANAGERS
UPDATE `organization` SET manager_id = 1 WHERE id = 1;
UPDATE `organization` SET manager_id = 4 WHERE id = 2;
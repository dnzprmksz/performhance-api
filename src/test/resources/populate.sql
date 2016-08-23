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
INSERT INTO `user`(name, password, role, surname, username, job_title_id, organization_id)
    VALUES('Forgan', '123', 'EMPLOYEE', 'Mreeman', 'bilge.ay', 5, 2);


--                  ORGANIZATION-USER INSERTIONS                   --
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 1);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 2);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 3);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 4);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(1, 5);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 6);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 7);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 8);
INSERT INTO `organization_users`(organization_id, users_id) VALUES(2, 9);


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
INSERT INTO `team_members`(team_id, members_id) VALUES(2, 9);


-- SETTING TEAMS
UPDATE `user` SET team_id = 1 WHERE id = 2;
UPDATE `user` SET team_id = 1 WHERE id = 3;
UPDATE `user` SET team_id = 1 WHERE id = 4;
UPDATE `user` SET team_id = 2 WHERE id = 7;
UPDATE `user` SET team_id = 2 WHERE id = 8;
UPDATE `user` SET team_id = 2 WHERE id = 9;


-- SETTING MANAGERS && NUMBER OF EMPLOYEES
UPDATE `organization` SET manager_id = 1, number_of_employees = 5 WHERE id = 1;
UPDATE `organization` SET manager_id = 6, number_of_employees = 4 WHERE id = 2;


--                       CRITERIA INSERTIONS                       --
INSERT INTO `criteria`(criteria, organization_id) VALUES('Manners', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Code Review', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Code Coverage', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Dress Code', 1);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Punctuality', 2);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Dedication', 2);
INSERT INTO `criteria`(criteria, organization_id) VALUES('Code Clarity', 2);


--              ORGANIZATION-CRITERIA LIST INSERTIONS              --
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,1);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,2);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,3);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(1,4);

INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(2,5);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(2,6);
INSERT INTO `organization_criteria_list`(organization_id, criteria_list_id) VALUES(2,7);


--                  USER-CRITERIA LIST INSERTIONS                  --

-- Pelin
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(2,1);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(2,3);

-- Faruk
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(3,1);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(3,2);

-- Pelya
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,1);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,2);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,3);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(4,4);

-- Bilge
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(7,5);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(7,6);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(7,7);

-- Yigit
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(8,5);
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(8,7);

-- Forgan
INSERT INTO `user_criteria_list`(user_id, criteria_list_id) VALUES(9,5);



--                        REVIEW INSERTIONS                        --

--Google Reviews
INSERT INTO `review`(comment, organization_id, reviewed_employee_id, reviewer_id, team_id)
    VALUES('She is harder better faster stronger this year.', 1, 2, 3, 1);
INSERT INTO `review`(comment, organization_id, reviewed_employee_id, reviewer_id, team_id)
    VALUES('He has no idea what he is doing most of the time.', 1, 3, 4, 1);
INSERT INTO `review`(comment, organization_id, reviewed_employee_id, reviewer_id, team_id)
    VALUES('Brings a new notion to post modern CS.', 1, 2, 4, 1);
INSERT INTO `review`(comment, organization_id, reviewed_employee_id, reviewer_id, team_id)
    VALUES('Impressive for a Russian.', 1, 4, 3, 1);

--Monitise Reviews
INSERT INTO `review`(comment, organization_id, reviewed_employee_id, reviewer_id, team_id)
    VALUES('I dont know why he is in the team, but this guy is awesome.', 2, 7, 8, 2);
INSERT INTO `review`(comment, organization_id, reviewed_employee_id, reviewer_id, team_id)
    VALUES('Gotta respect his resolve.', 2, 8, 9, 2);


--                      USER REVIEW INSERTIONS                     --
INSERT INTO `user_reviews`(user_id, reviews_id) VALUES(2, 1);
INSERT INTO `user_reviews`(user_id, reviews_id) VALUES(3, 2);
INSERT INTO `user_reviews`(user_id, reviews_id) VALUES(2, 3);
INSERT INTO `user_reviews`(user_id, reviews_id) VALUES(4, 4);

INSERT INTO `user_reviews`(user_id, reviews_id) VALUES(7, 5);
INSERT INTO `user_reviews`(user_id, reviews_id) VALUES(7, 6);


--                   REVIEW-EVALUATION INSERTIONS                  --
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(1, 95, 1);  -- Manners
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(1, 90, 3);  -- Code Coverage

INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(2, 20, 1);  -- Manners
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(2, 30, 2);  -- Code Review

INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(3, 90, 1);  -- Manners
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(3, 90, 3);  -- Code Coverage

INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(4, 80, 1);  -- Manners
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(4, 100, 2); -- Code Review
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(4, 97, 3);  -- Code Coverage
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(4, 100, 4); -- Dress Code

INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(5, 100, 5); -- Punctuality
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(5, 100, 6); -- Dedication
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(5, 100, 7); -- Code Clarity

INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(6, 100, 5); -- Punctuality
INSERT INTO `review_evaluation`(review_id, evaluation, evaluation_key) VALUES(6, 95, 7); -- Code Clarity

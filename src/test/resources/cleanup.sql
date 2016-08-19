SET FOREIGN_KEY_CHECKS=0;


TRUNCATE TABLE `criteria`;                  ALTER TABLE `criteria` ALTER COLUMN `id` RESTART WITH 1;
TRUNCATE TABLE `job_title`;                 ALTER TABLE `job_title` ALTER COLUMN `id` RESTART WITH 1;
TRUNCATE TABLE `organization`;              ALTER TABLE `organization` ALTER COLUMN `id` RESTART WITH 1;
TRUNCATE TABLE `organization_criteria_list`;
TRUNCATE TABLE `organization_teams`;
TRUNCATE TABLE `organization_users`;
TRUNCATE TABLE `organization_job_titles`;
TRUNCATE TABLE `review`;                    ALTER TABLE `review` ALTER COLUMN `id` RESTART WITH 1;
TRUNCATE TABLE `review_evaluation`;
TRUNCATE TABLE `team`;                      ALTER TABLE `team` ALTER COLUMN `id` RESTART WITH 1;
TRUNCATE TABLE `team_members`;
TRUNCATE TABLE `user`;                      ALTER TABLE `user` ALTER COLUMN `id` RESTART WITH 1;
TRUNCATE TABLE `user_criteria_list`;


SET FOREIGN_KEY_CHECKS=1;


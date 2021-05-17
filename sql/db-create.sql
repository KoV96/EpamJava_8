DROP TABLE IF EXISTS "users";

CREATE TABLE "users"("id" INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
"login" VARCHAR(255) NOT NULL UNIQUE);

INSERT INTO "users"
VALUES (1, 'ivanov');

DROP TABLE IF EXISTS "teams";

CREATE TABLE "teams"("id" INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
"name" VARCHAR(255) NOT NULL);

INSERT INTO "teams"
VALUES (1, 'teamA');

DROP TABLE IF EXISTS "users_teams";

CREATE TABLE "users_teams"("user_id" INT, FOREIGN KEY("user_id") REFERENCES "users"("id") ON DELETE CASCADE,
"team_id" INT, FOREIGN KEY("team_id") REFERENCES "teams"("id") ON DELETE CASCADE, UNIQUE("user_id", "team_id"));

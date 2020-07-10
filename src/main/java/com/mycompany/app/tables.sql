DROP TABLE IF EXISTS receivers;
DROP TABLE IF EXISTS contents;
DROP TABLE IF EXISTS emails;
DROP TABLE IF EXISTS people;


CREATE TABLE people (id SERIAL PRIMARY KEY, email_address VARCHAR(200), enron BOOLEAN DEFAULT 'f');

CREATE TABLE emails (id SERIAL PRIMARY KEY, sender_id INTEGER REFERENCES people(id), subject VARCHAR(500), date TIMESTAMP );

CREATE TABLE contents (email_id INTEGER REFERENCES emails(id), content TEXT);

CREATE TABLE receivers (email_id INTEGER REFERENCES emails(id), user_id INTEGER REFERENCES people(id));



\copy users to '/Users/annaleonenko/users3.csv' delimiter ',' csv header;
\copy emails to '/Users/annaleonenko/emails3.csv' delimiter ',' csv header;
\copy receivers to '/Users/annaleonenko/receivers3.csv' delimiter ',' csv header;
\copy people to '/Users/annaleonenko/people.csv' delimiter ',' csv header;
\copy contents to '/Users/annaleonenko/content.csv' delimiter ',' csv header;
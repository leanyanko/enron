
DROP TABLE IF EXISTS receivers;
DROP TABLE IF EXISTS contents;
DROP TABLE IF EXISTS emails_cleaned;
DROP TABLE IF EXISTS people;



CREATE TABLE people (id SERIAL PRIMARY KEY, email_address VARCHAR(200), enron BOOLEAN DEFAULT 'f');

CREATE TABLE emails_cleaned (id SERIAL PRIMARY KEY, sender_id INTEGER REFERENCES people(id), subject VARCHAR(500), date TIMESTAMP, forwarded BOOLEAN DEFAULT 'f', init_id INTEGER REFERENCES people(id));

CREATE TABLE contents (email_id INTEGER REFERENCES emails_cleaned(id), content TEXT);

CREATE TABLE receivers (email_id INTEGER REFERENCES emails_cleaned(id), user_id INTEGER REFERENCES people(id));



DROP TABLE IF EXISTS emails;
CREATE TABLE emails (id SERIAL PRIMARY KEY, sender_id INTEGER REFERENCES people(id), subject VARCHAR(500), date TIMESTAMP );
\copy users to '/Users/annaleonenko/users3.csv' delimiter ',' csv header;
\copy emails to '/Users/annaleonenko/emails3.csv' delimiter ',' csv header;
\copy receivers to '/Users/annaleonenko/receivers3.csv' delimiter ',' csv header;
\copy people to '/Users/annaleonenko/people.csv' delimiter ',' csv header;
\copy contents to '/Users/annaleonenko/content_cleaned.csv' delimiter ',' csv header;
\copy emails_cleaned to '/Users/annaleonenko/emails_cleaned.csv' delimiter ',' csv header;
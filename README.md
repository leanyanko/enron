# [WebSite](http://enron.s3-website-us-east-1.amazonaws.com/)

This project is creates to investigate relations within a subset of Enron employees email conversations happened between 1998 and 2001 years.

Project implemented with Java Maven, result stored at PostgreSQL

To setup local tables see 

`tables.sql`

to compile and run project:

`git clone https://github.com/meinou/enron.git`

before run insert your database credentials as a parameters of -java cp

`sh run.sh`

If use Tableau Desctop - connect your database directly for analysis

If use Tableau Public - download *.csv files for analysis as described in

`tables.sql`

## Project structure:

### Main code located at
`src/main/java/com/mycompany/app`

### Main class:
`App.java`

Illustrations:
![I1](https://github.com/meinou/enron/blob/master/screenshots/conversations.png)
![I2](https://github.com/meinou/enron/blob/master/screenshots/chains.png)
![I3](https://github.com/meinou/enron/blob/master/screenshots/people.png)
![I4](https://github.com/meinou/enron/blob/master/screenshots/employees.png)
![I5](https://github.com/meinou/enron/blob/master/screenshots/senders.png)

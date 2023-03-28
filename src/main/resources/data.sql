DROP TABLE IF EXISTS `_USER`;
DROP TABLE IF EXISTS `CATEGORY`;
DROP TABLE IF EXISTS `CONDITION`;


CREATE TABLE _USER (
                       id INT NOT NULL AUTO_INCREMENT,
                       nickname VARCHAR(255) NOT NULL,
                       phone_No VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       ZIP_CODE VARCHAR(255) NOT NULL,
                       `role` VARCHAR(255) NOT NULL,

                       PRIMARY KEY (id)
);

INSERT INTO _USER (nickname, phone_No, email, password, address,ZIP_CODE,role)
VALUES ('johny123', '45454545', 'john@john.com', '$2a$10$6tg6JUeGw8jFZqkqCuTjB.KGXSen/FXM9C3pk1mdKvsJDFtwmhdd2', 'Howitzvej 30', '2000','USER');



CREATE TABLE CATEGORY (
                       id INT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,


                       PRIMARY KEY (id)
);



INSERT INTO CATEGORY (name) VALUES ('Cars');
INSERT INTO CATEGORY (name) VALUES ('Camping');
INSERT INTO CATEGORY (name) VALUES ('Car accessories');
INSERT INTO CATEGORY (name) VALUES ('Housing');
INSERT INTO CATEGORY (name) VALUES ('Garden and barley');
INSERT INTO CATEGORY (name) VALUES ('Household');
INSERT INTO CATEGORY (name) VALUES ('For children');
INSERT INTO CATEGORY (name) VALUES ('Clothing and fashion');
INSERT INTO CATEGORY (name) VALUES ('Sports and leisure');
INSERT INTO CATEGORY (name) VALUES ('Bicycles');
INSERT INTO CATEGORY (name) VALUES ('Hobby');
INSERT INTO CATEGORY (name) VALUES ('Computer and game consoles');
INSERT INTO CATEGORY (name) VALUES ('Mobile and telephony');
INSERT INTO CATEGORY (name) VALUES ('Animals');
INSERT INTO CATEGORY (name) VALUES ('Picture and sound');
INSERT INTO CATEGORY (name) VALUES ('Musical instruments');
INSERT INTO CATEGORY (name) VALUES ('Motorcycles and accessories');
INSERT INTO CATEGORY (name) VALUES ('Scooters and mopeds');
INSERT INTO CATEGORY (name) VALUES ('Various profession');
INSERT INTO CATEGORY (name) VALUES ('Job');
INSERT INTO CATEGORY (name) VALUES ('Holiday');
INSERT INTO CATEGORY (name) VALUES ('Tickets');
INSERT INTO CATEGORY (name) VALUES ('Books and magazines');


CREATE TABLE CONDITION (
                           id INT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,


                           PRIMARY KEY (id)
);

INSERT INTO CONDITION (name) VALUES ('Like a new');
INSERT INTO CONDITION (name) VALUES ('Used in good condition');
INSERT INTO CONDITION (name) VALUES ('Used');
INSERT INTO CONDITION (name) VALUES ('Used in bad condition');





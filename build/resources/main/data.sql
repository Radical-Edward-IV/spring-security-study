-- ID, NAME, CREATED_DATE, LAST_MODIFIED_DATE
INSERT INTO ROLE VALUES (1, 'ADMIN', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO ROLE VALUES (2, 'USER', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO ROLE VALUES (3, 'DBA', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO ROLE VALUES (4, 'MODELER', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO ROLE VALUES (5, 'DEVELOPER', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- ID, FIRST_NAME, LAST_NAME, USERNAME, ENCRYPTED_PASSWORD, PHONE, EMAIL, ROLE, CREATED_DATE, LAST_MODIFIED_DATE
INSERT INTO USERS VALUES (1, 'Edward Se Jong Pepelu Tivrusky', 'Kim', 'admin001', '$2a$10$6h0Xp65w0OFNJp69DE0On.Fr0Dq54xAv36m7Eybt9g4EVt3S08L8m', '010-1234-5678', 'radical-edward@email.com', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO USERS VALUES (2, 'Theodore', 'Twombly', 'db_admin001', '$2a$10$N9HNSbMI5Eraa9MWzqoOluv2CNNRqK1DFufWuPxA8Y05HhB1.hLi6', '010-4433-6677', 'theo@email.com', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO USERS VALUES (3, 'Wade', 'Ripple', 'modeler001', '$2a$10$mCGOHUVABrwPxF5J5IxFCOND2O6fdC4QjyNUrbpXHge85GPKMfB62', '010-9876-5432', 'elemental@email.com', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO USERS VALUES (4, 'Sook', 'Kim', 'user0001', '$2a$10$DPILbfdpjspbIjY0no3edeBD6YZ7/dbGzm8j3WrMx0uboEdxitxfq', '010-2222-5555', 'dkdlemf@email.com', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO USERS VALUES (5, 'Sun', 'Im', 'developer001', '$2a$10$3o6GmStqp.JMm7KD9znn4OFwxShrrvEcE3G5wy2Iv9ZS68nrLYSZS', '010-8989-7777', 'sundal@email.com', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO USERS VALUES (6, 'Hyeok', 'Kim', 'developer002', '$2a$10$NTcsHcbMwJsRTWAwV7RkMuziwz.EGxKBafQ5eg2g5nSp97x5A3x6i', '010-6655-4433', 'blacksocks@email.com', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO USERS VALUES (7, 'Junny', 'Im', 'developer003', '$2a$10$MLelzeHADF7HCDIBtahUduneoTc3ClOkiuSThmmXm7G/3YvzfY5we', '010-8282-1199', 'junny@email.com', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- ID, ADDRESS_LINE_01, ADDRESS_LINE_02, CITY, STATE, ZIP_CODE, USER_ID, CREATE_DATE, CREATED_BY, LAST_MODIFIED_DATE, LAST_MODIFIED_BY
-- INSERT INTO ADDRESS VALUES(1, 'Fenn treasure', '39.117750, -106.445358', 'Colorado', 'Arizona', '81211', 1, CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1);
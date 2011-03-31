--slett gamle tabeller som er i veien
DROP VIEW V_meetings;
DROP TABLE attends;
DROP TABLE meetings;
DROP TABLE appointments;
DROP TABLE rooms;
DROP TABLE employees;


CREATE TABLE employees(
	username VARCHAR(30) NOT NULL,
	name VARCHAR(60),
	password VARCHAR(20),
	CONSTRAINT pk_employeesUsername PRIMARY KEY (username)
);


CREATE TABLE rooms(
	name VARCHAR(30) NOT NULL,
	size INTEGER,
	description CLOB,
	CONSTRAINT pk_roomsName PRIMARY KEY (name)
);	

CREATE TABLE appointments(
	id INTEGER GENERATED ALWAYS AS IDENTITY,
	employee VARCHAR(30) NOT NULL,
	addedDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	startDate TIMESTAMP NOT NULL,
	endDate TIMESTAMP NOT NULL,
	description CLOB, --character large object, kan vi klare oss med varchar?
	location VARCHAR(60),
	CONSTRAINT pk_appointmentsId PRIMARY KEY (id),
	CONSTRAINT fk_employeeAppointment FOREIGN KEY (employee)
		REFERENCES employees (username)
);


CREATE TABLE meetings(
	id INTEGER NOT NULL,
	room VARCHAR(30),
	CONSTRAINT pk_meetingsId PRIMARY KEY (id),
	CONSTRAINT fk_meetingsId FOREIGN KEY (id)
		REFERENCES appointments(id), --litt usikker på hvordan "arv" fungerer i sql...
	CONSTRAINT fk_roomMeeting FOREIGN KEY (room)
		REFERENCES rooms (name)
);

CREATE TABLE attends(
	username VARCHAR(30) NOT NULL,
	meetingId INTEGER NOT NULL,
	acceptanceStatus INTEGER NOT NULL,
	reason VARCHAR(60),
	CONSTRAINT fk_employeeAttends FOREIGN KEY (username)
		REFERENCES employees(username),
	CONSTRAINT fk_meetingsAttends FOREIGN KEY (meetingId)
		REFERENCES meetings(id),
	CONSTRAINT pk_attends PRIMARY KEY (username, meetingId)
);

CREATE VIEW V_meetings(id, employee, addedDate, startDate, endDate, description, location, room)
AS SELECT appointments.id, employee, addedDate, startDate, endDate, description, location, room
FROM appointments
JOIN meetings ON appointments.id=meetings.id;



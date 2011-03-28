INSERT INTO employees
VALUES(
	'kristian',
	'Kristian Nordbø Svendsen',
	'hemmelig'
);

INSERT INTO employees
VALUES(
	'katrine',
	'Katrine Olsen',
	'superhemmelig'
);

INSERT INTO appointments (employee, addedDate, startDate, endDate, description, location)
VALUES(
	'kristian',
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	'Skrive rapport, lang kjedelig beskrivelse',
	'På kontoret'
);

INSERT INTO appointments (employee, addedDate, startDate, endDate, description, location)
VALUES(
	'katrine',
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	'Viktig møte',
	'Med meg selv'
);

INSERT INTO rooms
VALUES(
	'vaskerommet',
	3,
	'Ruglete gulv, vaskemaskiner'
);


INSERT INTO appointments (employee, addedDate, startDate, endDate, description, location)
VALUES(
	'kristian',
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	'Kristian og katrinemøte',
	'Lite avsidesliggende rom'
);

INSERT INTO meetings
VALUES(
	3, --noen enkel måte å få tak i møte iden på?
	'vaskerommet'
);

INSERT INTO attends
VALUES(
	'katrine',
	3,
	0,
	NULL
);




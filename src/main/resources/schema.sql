DROP TABLE account;

CREATE TABLE IF NOT EXISTS account
(
    id       INT              NOT NULL,
    balance  DOUBLE PRECISION NOT NULL,
    currency TEXT             NOT NULL
);

CREATE TABLE IF NOT EXISTS bank_info
(
    id       UUID        NOT NULL,
    account  VARCHAR(28) NOT NULL,
    swift    VARCHAR(11) NOT NULL,
    owner_id UUID        NOT NULL,
    main     BOOLEAN     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (owner_id, account)
);

CREATE INDEX IF NOT EXISTS bank_info_owner_id_index ON bank_info (owner_id);
CREATE INDEX IF NOT EXISTS bank_info_owner_and_account_index ON bank_info (owner_id, account);

COMMIT;
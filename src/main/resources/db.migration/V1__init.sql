create table bekreftelse_arbeidsforhold
(
    id uuid primary key,
    fnr varchar,
    orgnr varchar
);

create table bekreftelse_arbeidsforhold_hendelse
(
    id uuid primary key,
    bekreftelse_id uuid not null,
    type varchar not null,
    sendt_inn_av varchar,
    stillingsprosent smallint not null,
    start_dato date,
    slutt_dato date
);

alter table bekreftelse_arbeidsforhold_hendelse
    add foreign key(bekreftelse_id) references bekreftelse_arbeidsforhold(id)

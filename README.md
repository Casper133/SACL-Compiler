# SACL-Compiler

SACL (***S**yntactically **A**wesome **C**onfig **L**anguage*) — config DSL, that can be transpiled to YAML.

Special thanks to ["Crafting Interpreters" handbook](https://craftinginterpreters.com).

## Code example

```
const RESTART_VALUE = on-failure
const NETWORK_NAME = link-shortener-network
const PostgreSQL_Port = 5432

version = "3.7"

services {
  link-shortener-service {
    build = .
    image = link-shortener:latest
    container_name = link-shortener-service
    restart = $RESTART_VALUE
    working_dir = /var/www/link_shortener

    environment {
      POSTGRES_DB_HOST = postgres-service
      POSTGRES_DB_PORT = $PostgreSQL_Port
      POSTGRES_DB_NAME = link_shortener
      POSTGRES_USERNAME = postgres
      POSTGRES_PASSWORD = postgres
      CURRENT_DOMAIN = "http://localhost"
    }

    volumes = ./:/var/www/link_shortener
    networks = $NETWORK_NAME
    depends_on = postgres-service
  }

  nginx-service {
    image = nginx:1.18.0-alpine
    container_name = nginx-service
    restart = $RESTART_VALUE
    ports = 80:80
    volumes = ./:/var/www/link_shortener
    networks = $NETWORK_NAME
    depends_on = link-shortener-service
  }

  postgres-service {
    image = postgres:12.0
    container_name = postgres-service
    restart = $RESTART_VALUE
    ports = $PostgreSQL_Port

    environment {
      POSTGRES_DB = link_shortener
      POSTGRES_USER = postgres
      POSTGRES_PASSWORD = postgres
    }

    volumes = postgres-link-shortener-data:/var/lib/postgresql/data
    networks = $NETWORK_NAME
  }
}

networks {
  link-shortener-network {
    driver = bridge
  }
}
```

#### It will be transpiled to YAML:

```yml
version: "3.7"
services:
  link-shortener-service:
    build: .
    image: link-shortener:latest
    container_name: link-shortener-service
    restart: on-failure
    working_dir: /var/www/link_shortener
    environment:
      POSTGRES_DB_HOST: postgres-service
      POSTGRES_DB_PORT: 5432
      POSTGRES_DB_NAME: link_shortener
      POSTGRES_USERNAME: postgres
      POSTGRES_PASSWORD: postgres
      CURRENT_DOMAIN: "http://localhost"
    volumes: ./:/var/www/link_shortener
    networks: link-shortener-network
    depends_on: postgres-service
  nginx-service:
    image: nginx:1.18.0-alpine
    container_name: nginx-service
    restart: on-failure
    ports: 80:80
    volumes: ./:/var/www/link_shortener
    networks: link-shortener-network
    depends_on: link-shortener-service
  postgres-service:
    image: postgres:12.0
    container_name: postgres-service
    restart: on-failure
    ports: 5432
    environment:
      POSTGRES_DB: link_shortener
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes: postgres-link-shortener-data:/var/lib/postgresql/data
    networks: link-shortener-network
networks:
  link-shortener-network:
    driver: bridge
```

## EBNF Grammar

```
/* Grammar */

source_code ::= LINES_SEPARATOR* constants_block? LINES_SEPARATOR+ config_block_body? LINES_SEPARATOR*

constants_block ::= constant_declaration (LINES_SEPARATOR+ constant_declaration)*

constant_declaration ::= CONST_KEYWORD WHITE_SPACE_CHARACTER+ identifier WHITE_SPACE_CHARACTER* record_declaration

CONST_KEYWORD ::= C_LETTER O_LETTER N_LETTER S_LETTER T_LETTER

config_block_body ::= WHITE_SPACE_CHARACTER* identifier WHITE_SPACE_CHARACTER*
    (config_block | record_declaration)
    (
      LINES_SEPARATOR+
      WHITE_SPACE_CHARACTER* identifier WHITE_SPACE_CHARACTER*
      (config_block | record_declaration)
    )*

config_block ::= LEFT_BRACE WHITE_SPACE_CHARACTER*
    LINES_SEPARATOR+ config_block_body LINES_SEPARATOR+
    WHITE_SPACE_CHARACTER* RIGHT_BRACE WHITE_SPACE_CHARACTER*

LINES_SEPARATOR ::= WHITE_SPACE_CHARACTER* LINE_BREAK_CHARACTER

record_declaration ::= EQUALS WHITE_SPACE_CHARACTER* record_value

record_value ::= constant_call | escaped_sequence? characters_sequence

constant_call ::= DOLLAR_SIGN identifier

identifier ::= (TEXT_CHARACTER
    | C_LETTER
    | O_LETTER
    | N_LETTER
    | S_LETTER
    | T_LETTER
    | LEFT_BRACE
    | RIGHT_BRACE
    | EQUALS
    | DOLLAR_SIGN
    | BACKSLASH)+

escaped_sequence ::= (BACKSLASH (BACKSLASH | DOLLAR_SIGN))+

characters_sequence ::= (TEXT_CHARACTER
    | C_LETTER
    | O_LETTER
    | N_LETTER
    | S_LETTER
    | T_LETTER
    | LEFT_BRACE
    | RIGHT_BRACE
    | EQUALS
    | DOLLAR_SIGN
    | BACKSLASH
    | WHITE_SPACE_CHARACTER)+


/* Tokens */

WHITE_SPACE_CHARACTER ::= #x09 | #x20         /* Tab | Space */

LINE_BREAK_CHARACTER ::= #x0D #x0A? | #x0A    /* CR LF | CR | LF */

/*
 Extended ASCII table without control characters

 Skipped symbols:
  #x24 - $
  #x3D - =
  #x5C - \
  #x63 - c
  #x6E - n
  #x6F - o
  #x73 - s
  #x74 - t
  #x7B - {
  #x7D - }
 */
TEXT_CHARACTER ::= [#x21-#x23]
    | [#x25-#x3C]
    | [#x3E-#x5B]
    | [#x5D-#x62]
    | [#x64-#x6D]
    | [#x70-#x72]
    | [#x75-#x7A]
    | #x7C
    | [#x7E-#xFF]

C_LETTER ::= "c"

O_LETTER ::= "o"

N_LETTER ::= "n"

S_LETTER ::= "s"

T_LETTER ::= "t"

LEFT_BRACE ::= "{"

RIGHT_BRACE ::= "}"

EQUALS ::= "="

DOLLAR_SIGN ::= "$"

BACKSLASH ::= "\"
```

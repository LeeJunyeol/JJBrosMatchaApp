-- ����Ǫ��Ʈ������
ALTER TABLE "PRIVATE_NOTICE"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_PRIVATE_NOTICE"; -- ���� -> ����Ǫ��Ʈ������

-- ����
ALTER TABLE "SCHEDULE"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_SCHEDULE"; -- ���� -> ����

-- ��������
ALTER TABLE "STAR_REVIEW"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_STAR_REVIEW"; -- ���� -> ��������

-- ������
ALTER TABLE "MYCOUPON"
	DROP CONSTRAINT IF EXISTS "FK_COUPON_TO_MYCOUPON"; -- ���� -> ������

-- ������
ALTER TABLE "MYCOUPON"
	DROP CONSTRAINT IF EXISTS "FK_MATCHA_USER_TO_MYCOUPON"; -- ���� -> ������

-- �ǽð���ġ
ALTER TABLE "REALTIME_LOCATION"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_REALTIME_LOCATION"; -- ���� -> �ǽð���ġ

-- ���
ALTER TABLE "COMMENT"
	DROP CONSTRAINT IF EXISTS "FK_STAR_REVIEW_TO_COMMENT"; -- �������� -> ���

-- ����
ALTER TABLE "COUPON"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_COUPON"; -- ���� -> ����

-- �޴�
ALTER TABLE "MENU"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_MENU"; -- ���� -> �޴�

-- ���ã�� Ǫ��Ʈ��
ALTER TABLE "BOOKMARK_TRUCK"
	DROP CONSTRAINT IF EXISTS "FK_MATCHA_USER_TO_BOOKMARK_TRUCK"; -- ���� -> ���ã�� Ǫ��Ʈ��

-- ���ã�� Ǫ��Ʈ��
ALTER TABLE "BOOKMARK_TRUCK"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_BOOKMARK_TRUCK"; -- ���� -> ���ã�� Ǫ��Ʈ��

-- ����
ALTER TABLE "OWNER"
	DROP CONSTRAINT IF EXISTS "PK_OWNER"; -- ���� �⺻Ű

-- ����Ǫ��Ʈ������
ALTER TABLE "PRIVATE_NOTICE"
	DROP CONSTRAINT IF EXISTS "PK_PRIVATE_NOTICE"; -- ����Ǫ��Ʈ������ �⺻Ű

-- ����
ALTER TABLE "SCHEDULE"
	DROP CONSTRAINT IF EXISTS "PK_SCHEDULE"; -- ���� �⺻Ű

-- ��������
ALTER TABLE "STAR_REVIEW"
	DROP CONSTRAINT IF EXISTS "PK_STAR_REVIEW"; -- �������� �⺻Ű

-- ������
ALTER TABLE "MYCOUPON"
	DROP CONSTRAINT IF EXISTS "PK_MYCOUPON"; -- ������ �⺻Ű

-- �̺�Ʈ ��������
ALTER TABLE "EVENT_NOTICE"
	DROP CONSTRAINT IF EXISTS "PK_EVENT_NOTICE"; -- �̺�Ʈ �������� �⺻Ű

-- �������
ALTER TABLE "FESTIVAL_INFO"
	DROP CONSTRAINT IF EXISTS "PK_FESTIVAL_INFO"; -- ������� �⺻Ű

-- ���
ALTER TABLE "COMMENT"
	DROP CONSTRAINT IF EXISTS "PK_COMMENT"; -- ��� �⺻Ű

-- ����
ALTER TABLE "MATCHA_USER"
	DROP CONSTRAINT IF EXISTS "PK_MATCHA_USER"; -- ���� �⺻Ű

-- ����
ALTER TABLE "COUPON"
	DROP CONSTRAINT IF EXISTS "PK_COUPON"; -- ���� �⺻Ű

-- �޴�
ALTER TABLE "MENU"
	DROP CONSTRAINT IF EXISTS "PK_MENU"; -- �޴� �⺻Ű

-- ����
DROP TABLE IF EXISTS "OWNER";

-- ����Ǫ��Ʈ������
DROP TABLE IF EXISTS "PRIVATE_NOTICE";

-- ����
DROP TABLE IF EXISTS "SCHEDULE";

-- ��������
DROP TABLE IF EXISTS "STAR_REVIEW";

-- ������
DROP TABLE IF EXISTS "MYCOUPON";

-- �̺�Ʈ ��������
DROP TABLE IF EXISTS "EVENT_NOTICE";

-- �������
DROP TABLE IF EXISTS "FESTIVAL_INFO";

-- �ǽð���ġ
DROP TABLE IF EXISTS "REALTIME_LOCATION";

-- ���
DROP TABLE IF EXISTS "COMMENT";

-- ����
DROP TABLE IF EXISTS "MATCHA_USER";

-- ����
DROP TABLE IF EXISTS "COUPON";

-- �޴�
DROP TABLE IF EXISTS "MENU";

-- ���ã�� Ǫ��Ʈ��
DROP TABLE IF EXISTS "BOOKMARK_TRUCK";

-- �� ��Ű��
DROP SCHEMA IF EXISTS "MY_SCHEMA" RESTRICT;

-- ����
CREATE TABLE "OWNER"
(
	"ID"              serial  NOT NULL, -- ����ID
	"EMAIL"           varchar NULL,     -- �̸���
	"PW"              varchar NULL,     -- ��й�ȣ
	"SEX"             boolean NULL,     -- ����
	"BIRTH"           date    NULL,     -- �������
	"NAME"            varchar NULL,     -- �̸�
	"PHONE"           int     NULL,     -- �޴�����ȣ
	"REG_NUM"         varchar NULL,     -- ��Ϲ�ȣ
	"MENU_CATEGORY"   varchar NULL,     -- �Ǹ���������
	"ADMITION_STATUS" boolean NULL      -- ���ο���
)
WITH (
OIDS=false
);

-- ���� �⺻Ű
CREATE UNIQUE INDEX "PK_OWNER"
	ON "OWNER"
	( -- ����
		"ID" ASC -- ����ID
	)
;
-- ����
ALTER TABLE "OWNER"
	ADD CONSTRAINT "PK_OWNER"
		 -- ���� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_OWNER";

-- ����Ǫ��Ʈ������
CREATE TABLE "PRIVATE_NOTICE"
(
	"ID"       serial  NOT NULL, -- ��������ID
	"TITLE"    varchar NULL,     -- ����
	"CONTENT"  varchar NULL,     -- ����
	"DATE"     date    NULL,     -- ��ϳ�¥
	"WRITER"   varchar NULL,     -- �ۼ���
	"CATEGORY" int     NULL,     -- �з�
	"OWNER_ID" serial  NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- ����Ǫ��Ʈ������ �⺻Ű
CREATE UNIQUE INDEX "PK_PRIVATE_NOTICE"
	ON "PRIVATE_NOTICE"
	( -- ����Ǫ��Ʈ������
		"ID" ASC -- ��������ID
	)
;
-- ����Ǫ��Ʈ������
ALTER TABLE "PRIVATE_NOTICE"
	ADD CONSTRAINT "PK_PRIVATE_NOTICE"
		 -- ����Ǫ��Ʈ������ �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_PRIVATE_NOTICE";

-- ����
CREATE TABLE "SCHEDULE"
(
	"ID"         serial      NOT NULL, -- ����ID
	"LOCATION"   point       NULL,     -- ��ġ
	"START_DATE" date        NULL,     -- ������
	"END_DATE"   date        NULL,     -- ������
	"START_TIME" time        NULL,     -- ���۽ð�
	"END_TIME"   time        NULL,     -- ����ð�
	"DAY"        varchar(50) NULL,     -- ����
	"REPEAT"     boolean     NULL,     -- �ݺ�����
	"OWNER_ID"   serial      NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- ���� �⺻Ű
CREATE UNIQUE INDEX "PK_SCHEDULE"
	ON "SCHEDULE"
	( -- ����
		"ID" ASC -- ����ID
	)
;
-- ����
ALTER TABLE "SCHEDULE"
	ADD CONSTRAINT "PK_SCHEDULE"
		 -- ���� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_SCHEDULE";

-- ��������
CREATE TABLE "STAR_REVIEW"
(
	"ID"       serial  NOT NULL, -- ��������ID
	"STAR"     float   NULL,     -- ������
	"REVIEW"   varchar NULL,     -- ����
	"DATE"     date    NULL,     -- �ۼ���
	"OWNER_ID" serial  NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- �������� �⺻Ű
CREATE UNIQUE INDEX "PK_STAR_REVIEW"
	ON "STAR_REVIEW"
	( -- ��������
		"ID" ASC -- ��������ID
	)
;
-- ��������
ALTER TABLE "STAR_REVIEW"
	ADD CONSTRAINT "PK_STAR_REVIEW"
		 -- �������� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_STAR_REVIEW";

-- ������
CREATE TABLE "MYCOUPON"
(
	"ID"         serial  NOT NULL, -- ������ID
	"USED"       boolean NULL,     -- ��뿩��
	"SERIAL_NUM" varchar NULL,     -- �ø����ȣ
	"COUPON_ID"  serial  NOT NULL, -- ����ID
	"USER_ID"    serial  NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- ������ �⺻Ű
CREATE UNIQUE INDEX "PK_MYCOUPON"
	ON "MYCOUPON"
	( -- ������
		"ID" ASC -- ������ID
	)
;
-- ������
ALTER TABLE "MYCOUPON"
	ADD CONSTRAINT "PK_MYCOUPON"
		 -- ������ �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_MYCOUPON";

-- �̺�Ʈ ��������
CREATE TABLE "EVENT_NOTICE"
(
	"ID"       serial  NOT NULL, -- �̺�Ʈ����ID
	"TITLE"    varchar NULL,     -- ����
	"CONTENT"  varchar NULL,     -- ����
	"DATE"     date    NULL,     -- �ۼ���
	"WRITER"   varchar NULL,     -- �ۼ���
	"CATEGORY" varchar NULL      -- �з�
)
WITH (
OIDS=false
);

-- �̺�Ʈ �������� �⺻Ű
CREATE UNIQUE INDEX "PK_EVENT_NOTICE"
	ON "EVENT_NOTICE"
	( -- �̺�Ʈ ��������
		"ID" ASC -- �̺�Ʈ����ID
	)
;
-- �̺�Ʈ ��������
ALTER TABLE "EVENT_NOTICE"
	ADD CONSTRAINT "PK_EVENT_NOTICE"
		 -- �̺�Ʈ �������� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_EVENT_NOTICE";

-- �������
CREATE TABLE "FESTIVAL_INFO"
(
	"ID"         serial  NOT NULL, -- �������ID
	"TITLE"      varchar NULL,     -- ����
	"CONTENT"    varchar NULL,     -- ����
	"START_DATE" date    NULL,     -- ������
	"END_DATE"   date    NULL,     -- ������
	"WRITER"     varchar NULL,     -- �ۼ���
	"LOCATION"   point   NULL      -- ��ġ
)
WITH (
OIDS=false
);

-- ������� �⺻Ű
CREATE UNIQUE INDEX "PK_FESTIVAL_INFO"
	ON "FESTIVAL_INFO"
	( -- �������
		"ID" ASC -- �������ID
	)
;
-- �������
ALTER TABLE "FESTIVAL_INFO"
	ADD CONSTRAINT "PK_FESTIVAL_INFO"
		 -- ������� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_FESTIVAL_INFO";

-- �ǽð���ġ
CREATE TABLE "REALTIME_LOCATION"
(
	"location" point  NULL,     -- �ǽð���ġ
	"OWNER_ID" serial NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- ���
CREATE TABLE "COMMENT"
(
	"ID"            serial  NOT NULL, -- ���ID
	"CONTENT"       varchar NULL,     -- ����
	"DATE"          date    NULL,     -- ��¥
	"WRITER"        varchar NULL,     -- �ۼ���
	"STARREVIEW_ID" serial  NOT NULL  -- ��������ID
)
WITH (
OIDS=false
);

-- ��� �⺻Ű
CREATE UNIQUE INDEX "PK_COMMENT"
	ON "COMMENT"
	( -- ���
		"ID" ASC -- ���ID
	)
;
-- ���
ALTER TABLE "COMMENT"
	ADD CONSTRAINT "PK_COMMENT"
		 -- ��� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_COMMENT";

-- ����
CREATE TABLE "MATCHA_USER"
(
	"ID"    serial  NOT NULL, -- ����ID
	"EMAIL" varchar NULL,     -- �̸���
	"PW"    varchar NULL,     -- ��й�ȣ
	"SEX"   boolean NULL,     -- ����
	"BIRTH" date    NULL      -- �������
)
WITH (
OIDS=false
);

-- ���� �⺻Ű
CREATE UNIQUE INDEX "PK_MATCHA_USER"
	ON "MATCHA_USER"
	( -- ����
		"ID" ASC -- ����ID
	)
;
-- ����
ALTER TABLE "MATCHA_USER"
	ADD CONSTRAINT "PK_MATCHA_USER"
		 -- ���� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_MATCHA_USER";

-- ����
CREATE TABLE "COUPON"
(
	"ID"         serial  NOT NULL, -- ����ID
	"NAME"       varchar NULL,     -- ������
	"START_DATE" date    NULL,     -- ��ȿ�Ⱓ������
	"END_DATE"   date    NULL,     -- ��ȿ�Ⱓ������
	"DETAIL"     varchar NULL,     -- ����
	"OWNER_ID"   serial  NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- ���� �⺻Ű
CREATE UNIQUE INDEX "PK_COUPON"
	ON "COUPON"
	( -- ����
		"ID" ASC -- ����ID
	)
;
-- ����
ALTER TABLE "COUPON"
	ADD CONSTRAINT "PK_COUPON"
		 -- ���� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_COUPON";

-- �޴�
CREATE TABLE "MENU"
(
	"ID"          serial  NOT NULL, -- �޴�ID
	"NAME"        varchar NULL,     -- �޴��̸�
	"PRICE"       int     NULL,     -- ����
	"INGREDIENTS" varchar NULL,     -- ���
	"DETAIL"      varchar NULL,     -- ����
	"STATUS"      int     NULL,     -- �Ż󿩺�
	"OWNER_ID"    serial  NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- �޴� �⺻Ű
CREATE UNIQUE INDEX "PK_MENU"
	ON "MENU"
	( -- �޴�
		"ID" ASC -- �޴�ID
	)
;
-- �޴�
ALTER TABLE "MENU"
	ADD CONSTRAINT "PK_MENU"
		 -- �޴� �⺻Ű
	PRIMARY KEY 
	USING INDEX "PK_MENU";

-- ���ã�� Ǫ��Ʈ��
CREATE TABLE "BOOKMARK_TRUCK"
(
	"USER_ID" serial NOT NULL, -- �����ID
	"ID"      serial NOT NULL  -- ����ID
)
WITH (
OIDS=false
);

-- ����Ǫ��Ʈ������
ALTER TABLE "PRIVATE_NOTICE"
	ADD CONSTRAINT "FK_OWNER_TO_PRIVATE_NOTICE"
	 -- ���� -> ����Ǫ��Ʈ������
		FOREIGN KEY (
			"OWNER_ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ����
ALTER TABLE "SCHEDULE"
	ADD CONSTRAINT "FK_OWNER_TO_SCHEDULE"
	 -- ���� -> ����
		FOREIGN KEY (
			"OWNER_ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ��������
ALTER TABLE "STAR_REVIEW"
	ADD CONSTRAINT "FK_OWNER_TO_STAR_REVIEW"
	 -- ���� -> ��������
		FOREIGN KEY (
			"OWNER_ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ������
ALTER TABLE "MYCOUPON"
	ADD CONSTRAINT "FK_COUPON_TO_MYCOUPON"
	 -- ���� -> ������
		FOREIGN KEY (
			"COUPON_ID" -- ����ID
		)
		REFERENCES "COUPON" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ������
ALTER TABLE "MYCOUPON"
	ADD CONSTRAINT "FK_MATCHA_USER_TO_MYCOUPON"
	 -- ���� -> ������
		FOREIGN KEY (
			"USER_ID" -- ����ID
		)
		REFERENCES "MATCHA_USER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- �ǽð���ġ
ALTER TABLE "REALTIME_LOCATION"
	ADD CONSTRAINT "FK_OWNER_TO_REALTIME_LOCATION"
	 -- ���� -> �ǽð���ġ
		FOREIGN KEY (
			"OWNER_ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ���
ALTER TABLE "COMMENT"
	ADD CONSTRAINT "FK_STAR_REVIEW_TO_COMMENT"
	 -- �������� -> ���
		FOREIGN KEY (
			"STARREVIEW_ID" -- ��������ID
		)
		REFERENCES "STAR_REVIEW" ( -- ��������
			"ID" -- ��������ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ����
ALTER TABLE "COUPON"
	ADD CONSTRAINT "FK_OWNER_TO_COUPON"
	 -- ���� -> ����
		FOREIGN KEY (
			"OWNER_ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- �޴�
ALTER TABLE "MENU"
	ADD CONSTRAINT "FK_OWNER_TO_MENU"
	 -- ���� -> �޴�
		FOREIGN KEY (
			"OWNER_ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ���ã�� Ǫ��Ʈ��
ALTER TABLE "BOOKMARK_TRUCK"
	ADD CONSTRAINT "FK_MATCHA_USER_TO_BOOKMARK_TRUCK"
	 -- ���� -> ���ã�� Ǫ��Ʈ��
		FOREIGN KEY (
			"USER_ID" -- �����ID
		)
		REFERENCES "MATCHA_USER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- ���ã�� Ǫ��Ʈ��
ALTER TABLE "BOOKMARK_TRUCK"
	ADD CONSTRAINT "FK_OWNER_TO_BOOKMARK_TRUCK"
	 -- ���� -> ���ã�� Ǫ��Ʈ��
		FOREIGN KEY (
			"ID" -- ����ID
		)
		REFERENCES "OWNER" ( -- ����
			"ID" -- ����ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

ALTER TABLE "OWNER" alter COLUMN "PHONE" type VARCHAR;
ALTER TABLE "OWNER" alter COLUMN "REG_NUM" type VARCHAR;
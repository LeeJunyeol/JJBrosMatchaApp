-- 개별푸드트럭공지
ALTER TABLE "PRIVATE_NOTICE"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_PRIVATE_NOTICE"; -- 업주 -> 개별푸드트럭공지

-- 일정
ALTER TABLE "SCHEDULE"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_SCHEDULE"; -- 업주 -> 일정

-- 평점리뷰
ALTER TABLE "STAR_REVIEW"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_STAR_REVIEW"; -- 업주 -> 평점리뷰

-- 내쿠폰
ALTER TABLE "MYCOUPON"
	DROP CONSTRAINT IF EXISTS "FK_COUPON_TO_MYCOUPON"; -- 쿠폰 -> 내쿠폰

-- 내쿠폰
ALTER TABLE "MYCOUPON"
	DROP CONSTRAINT IF EXISTS "FK_MATCHA_USER_TO_MYCOUPON"; -- 유저 -> 내쿠폰

-- 실시간위치
ALTER TABLE "REALTIME_LOCATION"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_REALTIME_LOCATION"; -- 업주 -> 실시간위치

-- 댓글
ALTER TABLE "COMMENT"
	DROP CONSTRAINT IF EXISTS "FK_STAR_REVIEW_TO_COMMENT"; -- 평점리뷰 -> 댓글

-- 쿠폰
ALTER TABLE "COUPON"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_COUPON"; -- 업주 -> 쿠폰

-- 메뉴
ALTER TABLE "MENU"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_MENU"; -- 업주 -> 메뉴

-- 즐겨찾는 푸드트럭
ALTER TABLE "BOOKMARK_TRUCK"
	DROP CONSTRAINT IF EXISTS "FK_MATCHA_USER_TO_BOOKMARK_TRUCK"; -- 유저 -> 즐겨찾는 푸드트럭

-- 즐겨찾는 푸드트럭
ALTER TABLE "BOOKMARK_TRUCK"
	DROP CONSTRAINT IF EXISTS "FK_OWNER_TO_BOOKMARK_TRUCK"; -- 업주 -> 즐겨찾는 푸드트럭

-- 업주
ALTER TABLE "OWNER"
	DROP CONSTRAINT IF EXISTS "PK_OWNER"; -- 업주 기본키

-- 개별푸드트럭공지
ALTER TABLE "PRIVATE_NOTICE"
	DROP CONSTRAINT IF EXISTS "PK_PRIVATE_NOTICE"; -- 개별푸드트럭공지 기본키

-- 일정
ALTER TABLE "SCHEDULE"
	DROP CONSTRAINT IF EXISTS "PK_SCHEDULE"; -- 일정 기본키

-- 평점리뷰
ALTER TABLE "STAR_REVIEW"
	DROP CONSTRAINT IF EXISTS "PK_STAR_REVIEW"; -- 평점리뷰 기본키

-- 내쿠폰
ALTER TABLE "MYCOUPON"
	DROP CONSTRAINT IF EXISTS "PK_MYCOUPON"; -- 내쿠폰 기본키

-- 이벤트 공지사항
ALTER TABLE "EVENT_NOTICE"
	DROP CONSTRAINT IF EXISTS "PK_EVENT_NOTICE"; -- 이벤트 공지사항 기본키

-- 행사정보
ALTER TABLE "FESTIVAL_INFO"
	DROP CONSTRAINT IF EXISTS "PK_FESTIVAL_INFO"; -- 행사정보 기본키

-- 댓글
ALTER TABLE "COMMENT"
	DROP CONSTRAINT IF EXISTS "PK_COMMENT"; -- 댓글 기본키

-- 유저
ALTER TABLE "MATCHA_USER"
	DROP CONSTRAINT IF EXISTS "PK_MATCHA_USER"; -- 유저 기본키

-- 쿠폰
ALTER TABLE "COUPON"
	DROP CONSTRAINT IF EXISTS "PK_COUPON"; -- 쿠폰 기본키

-- 메뉴
ALTER TABLE "MENU"
	DROP CONSTRAINT IF EXISTS "PK_MENU"; -- 메뉴 기본키

-- 업주
DROP TABLE IF EXISTS "OWNER";

-- 개별푸드트럭공지
DROP TABLE IF EXISTS "PRIVATE_NOTICE";

-- 일정
DROP TABLE IF EXISTS "SCHEDULE";

-- 평점리뷰
DROP TABLE IF EXISTS "STAR_REVIEW";

-- 내쿠폰
DROP TABLE IF EXISTS "MYCOUPON";

-- 이벤트 공지사항
DROP TABLE IF EXISTS "EVENT_NOTICE";

-- 행사정보
DROP TABLE IF EXISTS "FESTIVAL_INFO";

-- 실시간위치
DROP TABLE IF EXISTS "REALTIME_LOCATION";

-- 댓글
DROP TABLE IF EXISTS "COMMENT";

-- 유저
DROP TABLE IF EXISTS "MATCHA_USER";

-- 쿠폰
DROP TABLE IF EXISTS "COUPON";

-- 메뉴
DROP TABLE IF EXISTS "MENU";

-- 즐겨찾는 푸드트럭
DROP TABLE IF EXISTS "BOOKMARK_TRUCK";

-- 내 스키마
DROP SCHEMA IF EXISTS "MY_SCHEMA" RESTRICT;

-- 업주
CREATE TABLE "OWNER"
(
	"ID"              serial  NOT NULL, -- 업주ID
	"EMAIL"           varchar NULL,     -- 이메일
	"PW"              varchar NULL,     -- 비밀번호
	"SEX"             boolean NULL,     -- 성별
	"BIRTH"           date    NULL,     -- 생년월일
	"NAME"            varchar NULL,     -- 이름
	"PHONE"           int     NULL,     -- 휴대폰번호
	"REG_NUM"         varchar NULL,     -- 등록번호
	"MENU_CATEGORY"   varchar NULL,     -- 판매음식종류
	"ADMITION_STATUS" boolean NULL      -- 승인여부
)
WITH (
OIDS=false
);

-- 업주 기본키
CREATE UNIQUE INDEX "PK_OWNER"
	ON "OWNER"
	( -- 업주
		"ID" ASC -- 업주ID
	)
;
-- 업주
ALTER TABLE "OWNER"
	ADD CONSTRAINT "PK_OWNER"
		 -- 업주 기본키
	PRIMARY KEY 
	USING INDEX "PK_OWNER";

-- 개별푸드트럭공지
CREATE TABLE "PRIVATE_NOTICE"
(
	"ID"       serial  NOT NULL, -- 개별공지ID
	"TITLE"    varchar NULL,     -- 제목
	"CONTENT"  varchar NULL,     -- 내용
	"DATE"     date    NULL,     -- 등록날짜
	"WRITER"   varchar NULL,     -- 작성자
	"CATEGORY" int     NULL,     -- 분류
	"OWNER_ID" serial  NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 개별푸드트럭공지 기본키
CREATE UNIQUE INDEX "PK_PRIVATE_NOTICE"
	ON "PRIVATE_NOTICE"
	( -- 개별푸드트럭공지
		"ID" ASC -- 개별공지ID
	)
;
-- 개별푸드트럭공지
ALTER TABLE "PRIVATE_NOTICE"
	ADD CONSTRAINT "PK_PRIVATE_NOTICE"
		 -- 개별푸드트럭공지 기본키
	PRIMARY KEY 
	USING INDEX "PK_PRIVATE_NOTICE";

-- 일정
CREATE TABLE "SCHEDULE"
(
	"ID"         serial      NOT NULL, -- 일정ID
	"LOCATION"   point       NULL,     -- 위치
	"START_DATE" date        NULL,     -- 시작일
	"END_DATE"   date        NULL,     -- 종료일
	"START_TIME" time        NULL,     -- 시작시간
	"END_TIME"   time        NULL,     -- 종료시간
	"DAY"        varchar(50) NULL,     -- 요일
	"REPEAT"     boolean     NULL,     -- 반복여부
	"OWNER_ID"   serial      NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 일정 기본키
CREATE UNIQUE INDEX "PK_SCHEDULE"
	ON "SCHEDULE"
	( -- 일정
		"ID" ASC -- 일정ID
	)
;
-- 일정
ALTER TABLE "SCHEDULE"
	ADD CONSTRAINT "PK_SCHEDULE"
		 -- 일정 기본키
	PRIMARY KEY 
	USING INDEX "PK_SCHEDULE";

-- 평점리뷰
CREATE TABLE "STAR_REVIEW"
(
	"ID"       serial  NOT NULL, -- 평점리뷰ID
	"STAR"     float   NULL,     -- 별점수
	"REVIEW"   varchar NULL,     -- 리뷰
	"DATE"     date    NULL,     -- 작성일
	"OWNER_ID" serial  NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 평점리뷰 기본키
CREATE UNIQUE INDEX "PK_STAR_REVIEW"
	ON "STAR_REVIEW"
	( -- 평점리뷰
		"ID" ASC -- 평점리뷰ID
	)
;
-- 평점리뷰
ALTER TABLE "STAR_REVIEW"
	ADD CONSTRAINT "PK_STAR_REVIEW"
		 -- 평점리뷰 기본키
	PRIMARY KEY 
	USING INDEX "PK_STAR_REVIEW";

-- 내쿠폰
CREATE TABLE "MYCOUPON"
(
	"ID"         serial  NOT NULL, -- 내쿠폰ID
	"USED"       boolean NULL,     -- 사용여부
	"SERIAL_NUM" varchar NULL,     -- 시리얼번호
	"COUPON_ID"  serial  NOT NULL, -- 쿠폰ID
	"USER_ID"    serial  NOT NULL  -- 유저ID
)
WITH (
OIDS=false
);

-- 내쿠폰 기본키
CREATE UNIQUE INDEX "PK_MYCOUPON"
	ON "MYCOUPON"
	( -- 내쿠폰
		"ID" ASC -- 내쿠폰ID
	)
;
-- 내쿠폰
ALTER TABLE "MYCOUPON"
	ADD CONSTRAINT "PK_MYCOUPON"
		 -- 내쿠폰 기본키
	PRIMARY KEY 
	USING INDEX "PK_MYCOUPON";

-- 이벤트 공지사항
CREATE TABLE "EVENT_NOTICE"
(
	"ID"       serial  NOT NULL, -- 이벤트공지ID
	"TITLE"    varchar NULL,     -- 제목
	"CONTENT"  varchar NULL,     -- 내용
	"DATE"     date    NULL,     -- 작성일
	"WRITER"   varchar NULL,     -- 작성자
	"CATEGORY" varchar NULL      -- 분류
)
WITH (
OIDS=false
);

-- 이벤트 공지사항 기본키
CREATE UNIQUE INDEX "PK_EVENT_NOTICE"
	ON "EVENT_NOTICE"
	( -- 이벤트 공지사항
		"ID" ASC -- 이벤트공지ID
	)
;
-- 이벤트 공지사항
ALTER TABLE "EVENT_NOTICE"
	ADD CONSTRAINT "PK_EVENT_NOTICE"
		 -- 이벤트 공지사항 기본키
	PRIMARY KEY 
	USING INDEX "PK_EVENT_NOTICE";

-- 행사정보
CREATE TABLE "FESTIVAL_INFO"
(
	"ID"         serial  NOT NULL, -- 행사정보ID
	"TITLE"      varchar NULL,     -- 제목
	"CONTENT"    varchar NULL,     -- 내용
	"START_DATE" date    NULL,     -- 시작일
	"END_DATE"   date    NULL,     -- 종료일
	"WRITER"     varchar NULL,     -- 작성자
	"LOCATION"   point   NULL      -- 위치
)
WITH (
OIDS=false
);

-- 행사정보 기본키
CREATE UNIQUE INDEX "PK_FESTIVAL_INFO"
	ON "FESTIVAL_INFO"
	( -- 행사정보
		"ID" ASC -- 행사정보ID
	)
;
-- 행사정보
ALTER TABLE "FESTIVAL_INFO"
	ADD CONSTRAINT "PK_FESTIVAL_INFO"
		 -- 행사정보 기본키
	PRIMARY KEY 
	USING INDEX "PK_FESTIVAL_INFO";

-- 실시간위치
CREATE TABLE "REALTIME_LOCATION"
(
	"location" point  NULL,     -- 실시간위치
	"OWNER_ID" serial NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 댓글
CREATE TABLE "COMMENT"
(
	"ID"            serial  NOT NULL, -- 댓글ID
	"CONTENT"       varchar NULL,     -- 내용
	"DATE"          date    NULL,     -- 날짜
	"WRITER"        varchar NULL,     -- 작성자
	"STARREVIEW_ID" serial  NOT NULL  -- 평점리뷰ID
)
WITH (
OIDS=false
);

-- 댓글 기본키
CREATE UNIQUE INDEX "PK_COMMENT"
	ON "COMMENT"
	( -- 댓글
		"ID" ASC -- 댓글ID
	)
;
-- 댓글
ALTER TABLE "COMMENT"
	ADD CONSTRAINT "PK_COMMENT"
		 -- 댓글 기본키
	PRIMARY KEY 
	USING INDEX "PK_COMMENT";

-- 유저
CREATE TABLE "MATCHA_USER"
(
	"ID"    serial  NOT NULL, -- 유저ID
	"EMAIL" varchar NULL,     -- 이메일
	"PW"    varchar NULL,     -- 비밀번호
	"SEX"   boolean NULL,     -- 성별
	"BIRTH" date    NULL      -- 생년월일
)
WITH (
OIDS=false
);

-- 유저 기본키
CREATE UNIQUE INDEX "PK_MATCHA_USER"
	ON "MATCHA_USER"
	( -- 유저
		"ID" ASC -- 유저ID
	)
;
-- 유저
ALTER TABLE "MATCHA_USER"
	ADD CONSTRAINT "PK_MATCHA_USER"
		 -- 유저 기본키
	PRIMARY KEY 
	USING INDEX "PK_MATCHA_USER";

-- 쿠폰
CREATE TABLE "COUPON"
(
	"ID"         serial  NOT NULL, -- 쿠폰ID
	"NAME"       varchar NULL,     -- 쿠폰명
	"START_DATE" date    NULL,     -- 유효기간시작일
	"END_DATE"   date    NULL,     -- 유효기간종료일
	"DETAIL"     varchar NULL,     -- 설명
	"OWNER_ID"   serial  NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 쿠폰 기본키
CREATE UNIQUE INDEX "PK_COUPON"
	ON "COUPON"
	( -- 쿠폰
		"ID" ASC -- 쿠폰ID
	)
;
-- 쿠폰
ALTER TABLE "COUPON"
	ADD CONSTRAINT "PK_COUPON"
		 -- 쿠폰 기본키
	PRIMARY KEY 
	USING INDEX "PK_COUPON";

-- 메뉴
CREATE TABLE "MENU"
(
	"ID"          serial  NOT NULL, -- 메뉴ID
	"NAME"        varchar NULL,     -- 메뉴이름
	"PRICE"       int     NULL,     -- 가격
	"INGREDIENTS" varchar NULL,     -- 재료
	"DETAIL"      varchar NULL,     -- 설명
	"STATUS"      int     NULL,     -- 신상여부
	"OWNER_ID"    serial  NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 메뉴 기본키
CREATE UNIQUE INDEX "PK_MENU"
	ON "MENU"
	( -- 메뉴
		"ID" ASC -- 메뉴ID
	)
;
-- 메뉴
ALTER TABLE "MENU"
	ADD CONSTRAINT "PK_MENU"
		 -- 메뉴 기본키
	PRIMARY KEY 
	USING INDEX "PK_MENU";

-- 즐겨찾는 푸드트럭
CREATE TABLE "BOOKMARK_TRUCK"
(
	"USER_ID" serial NOT NULL, -- 사용자ID
	"ID"      serial NOT NULL  -- 업주ID
)
WITH (
OIDS=false
);

-- 개별푸드트럭공지
ALTER TABLE "PRIVATE_NOTICE"
	ADD CONSTRAINT "FK_OWNER_TO_PRIVATE_NOTICE"
	 -- 업주 -> 개별푸드트럭공지
		FOREIGN KEY (
			"OWNER_ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 일정
ALTER TABLE "SCHEDULE"
	ADD CONSTRAINT "FK_OWNER_TO_SCHEDULE"
	 -- 업주 -> 일정
		FOREIGN KEY (
			"OWNER_ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 평점리뷰
ALTER TABLE "STAR_REVIEW"
	ADD CONSTRAINT "FK_OWNER_TO_STAR_REVIEW"
	 -- 업주 -> 평점리뷰
		FOREIGN KEY (
			"OWNER_ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 내쿠폰
ALTER TABLE "MYCOUPON"
	ADD CONSTRAINT "FK_COUPON_TO_MYCOUPON"
	 -- 쿠폰 -> 내쿠폰
		FOREIGN KEY (
			"COUPON_ID" -- 쿠폰ID
		)
		REFERENCES "COUPON" ( -- 쿠폰
			"ID" -- 쿠폰ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 내쿠폰
ALTER TABLE "MYCOUPON"
	ADD CONSTRAINT "FK_MATCHA_USER_TO_MYCOUPON"
	 -- 유저 -> 내쿠폰
		FOREIGN KEY (
			"USER_ID" -- 유저ID
		)
		REFERENCES "MATCHA_USER" ( -- 유저
			"ID" -- 유저ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 실시간위치
ALTER TABLE "REALTIME_LOCATION"
	ADD CONSTRAINT "FK_OWNER_TO_REALTIME_LOCATION"
	 -- 업주 -> 실시간위치
		FOREIGN KEY (
			"OWNER_ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 댓글
ALTER TABLE "COMMENT"
	ADD CONSTRAINT "FK_STAR_REVIEW_TO_COMMENT"
	 -- 평점리뷰 -> 댓글
		FOREIGN KEY (
			"STARREVIEW_ID" -- 평점리뷰ID
		)
		REFERENCES "STAR_REVIEW" ( -- 평점리뷰
			"ID" -- 평점리뷰ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 쿠폰
ALTER TABLE "COUPON"
	ADD CONSTRAINT "FK_OWNER_TO_COUPON"
	 -- 업주 -> 쿠폰
		FOREIGN KEY (
			"OWNER_ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 메뉴
ALTER TABLE "MENU"
	ADD CONSTRAINT "FK_OWNER_TO_MENU"
	 -- 업주 -> 메뉴
		FOREIGN KEY (
			"OWNER_ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 즐겨찾는 푸드트럭
ALTER TABLE "BOOKMARK_TRUCK"
	ADD CONSTRAINT "FK_MATCHA_USER_TO_BOOKMARK_TRUCK"
	 -- 유저 -> 즐겨찾는 푸드트럭
		FOREIGN KEY (
			"USER_ID" -- 사용자ID
		)
		REFERENCES "MATCHA_USER" ( -- 유저
			"ID" -- 유저ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;

-- 즐겨찾는 푸드트럭
ALTER TABLE "BOOKMARK_TRUCK"
	ADD CONSTRAINT "FK_OWNER_TO_BOOKMARK_TRUCK"
	 -- 업주 -> 즐겨찾는 푸드트럭
		FOREIGN KEY (
			"ID" -- 업주ID
		)
		REFERENCES "OWNER" ( -- 업주
			"ID" -- 업주ID
		)
		ON UPDATE CASCADE ON DELETE CASCADE
		NOT VALID;
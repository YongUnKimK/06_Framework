--DROP TABLE TB_TODO;

-- 테이블 생성
CREATE TABLE TB_TODO(
	TODO_NO NUMBER,
	TODO_TITLE VARCHAR2(200),
	TODO_CONTENT VARCHAR2(4000),
	COMPLETE CHAR(1) DEFAULT 'N',
	REG_DATE DATE DEFAULT SYSDATE
);

-- 제약 조건 추가
ALTER TABLE TB_TODO
ADD CONSTRAINT TB_TODO_PK PRIMARY KEY(TODO_NO);

ALTER TABLE TB_TODO
MODIFY TODO_TITLE VARCHAR2(200) CONSTRAINT TODO_TITLE_NN NOT NULL;

ALTER TABLE TB_TODO
MODIFY TODO_CONTENT VARCHAR2(4000) CONSTRAINT TODO_CONTENT_NN NOT NULL;

ALTER TABLE TB_TODO
ADD CONSTRAINT COMPLETE_CHECK 
CHECK(COMPLETE IN ('N', 'Y') );

COMMENT ON COLUMN TB_TODO.TODO_NO IS '할 일 번호';
COMMENT ON COLUMN TB_TODO.TODO_TITLE IS '할 일 제목';
COMMENT ON COLUMN TB_TODO.TODO_CONTENT IS '할 일 내용';
COMMENT ON COLUMN TB_TODO.COMPLETE IS '할 일 완료 여부(Y/N)';
COMMENT ON COLUMN TB_TODO.REG_DATE IS '할 일 등록일';

-- 시퀀스 생성
CREATE SEQUENCE SEQ_TODO_NO NOCACHE;

-- 샘플 데이터
INSERT INTO TB_TODO
VALUES(SEQ_TODO_NO.NEXTVAL, '테스트1', '테스트1 입니다.', DEFAULT, DEFAULT);

INSERT INTO TB_TODO
VALUES(SEQ_TODO_NO.NEXTVAL, '테스트2', '테스트2 입니다.', DEFAULT, DEFAULT);

INSERT INTO TB_TODO
VALUES(SEQ_TODO_NO.NEXTVAL, '테스트3', '테스트3 입니다.', DEFAULT, DEFAULT);

INSERT INTO TB_TODO
VALUES(SEQ_TODO_NO.NEXTVAL, '테스트4', '테스트4 입니다.', DEFAULT, DEFAULT);

COMMIT;


 	SELECT TODO_TITLE FROM TB_TODO
  	WHERE TODO_NO = 1;
SELECT * FROM TB_TODO;


SELECT TODO_NO ,TODO_TITLE ,
COMPLETE,
TO_CHAR(REG_DATE, 'YYYY-MM-DD HH24:MI:SS') REG_DATE 
FROM TB_TODO 
ORDER BY TODO_NO;




# 전략패턴 기반 게시판 시스템

## 과제 개요
전략패턴(Strategy Pattern)을 활용하여 무한스크롤과 페이징 두 가지 게시글 로딩 전략을 선택적으로 제공하는 게시판 리스트 시스템입니다. 사용자는 UI에서 원하는 로딩 방식을 선택할 수 있으며, 백엔드는 전략 패턴을 적용하여 각 전략을 독립적으로 처리합니다.

## 기술 스택

### 백엔드
- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **H2 Database (인메모리)**
- **RESTful API**
- **전략패턴 설계 및 적용**

### 프론트엔드
- **React 18**
- **Material-UI (MUI)**
- **Axios**
- **TypeScript**
- **React Router**

## 구현된 기능

### ✅ 공통 요구사항
- [x] 모든 API는 RESTful 원칙을 따르고 JSON 형식으로 통신
- [x] CORS 허용 설정 포함
- [x] 프론트엔드와 백엔드 간의 통신 정상 작동

### ✅ 백엔드 기능
- [x] **게시글 List API**
  - 필드: id, title, content, author, createdAt
  - `GET /api/board` - 게시글 목록 조회 (페이징/무한스크롤)
  - `GET /api/board/{id}` - 단일 게시글 조회

- [x] **전략패턴 적용**
  - `LoadStrategy` 인터페이스 정의
  - `PagingStrategy` - 페이징 방식 구현
  - `InfiniteScrollStrategy` - 무한스크롤 방식 구현
  - `StrategyResolver` - 전략 선택 및 주입

- [x] **예외 처리**
  - Spring Validation 적용 (`@Valid`, `@NotNull`)
  - 존재하지 않는 게시글에 대한 404 응답 처리
  - 전역 예외 처리 (`GlobalExceptionHandler`)
  - 구조화된 에러 응답 (`ErrorCode`, `ExceptionDto`)

### ✅ 프론트엔드 기능
- [x] **게시글 목록 화면**
  - Material-UI Card를 사용한 게시글 카드 형태 표시
  - 반응형 디자인

- [x] **전략 선택 토글 UI**
  - AppBar에 전략 선택 토글 구현
  - 페이징/무한스크롤 전환 기능

- [x] **추가 구현 기능**
  - 게시글 상세 페이지
  - 에러 처리 및 사용자 알림
  - 로딩 상태 표시

## 프로젝트 구조

```
board-penta/
├── board-be/                    # Spring Boot 백엔드
│   ├── src/main/java/com/board/
│   │   ├── domain/
│   │   │   ├── controller/      # REST API 컨트롤러
│   │   │   ├── service/
│   │   │   │   └── strategy/    # 전략패턴 구현
│   │   │   │       ├── LoadStrategy.java
│   │   │   │       ├── LoadStrategyType.java
│   │   │   │       ├── StrategyResolver.java
│   │   │   │       └── impl/
│   │   │   │           ├── PagingStrategy.java
│   │   │   │           └── InfiniteScrollStrategy.java
│   │   │   ├── entity/          # JPA 엔티티
│   │   │   ├── dto/             # 데이터 전송 객체
│   │   │   └── repository/      # 데이터 접근 계층
│   │   └── global/common/       # 전역 설정 및 예외 처리
│   └── src/test/java/           # 테스트 코드
└── board-fe/                    # React 프론트엔드
    ├── src/
    │   ├── components/          # React 컴포넌트
    │   │   ├── BoardList.tsx    # 게시글 목록
    │   │   ├── BoardCard.tsx    # 게시글 카드
    │   │   ├── StrategyToggle.tsx # 전략 선택 토글
    │   │   └── Pagination.tsx   # 페이징 컴포넌트
    │   ├── api/                 # API 통신
    │   └── types/               # TypeScript 타입 정의
    └── Dockerfile               # 프론트엔드 Docker 설정
```

## 전략패턴 구현

### 백엔드 전략패턴 구조
```java
// 전략 인터페이스
public interface LoadStrategy {
    Page<BoardDto.Response> load(Pageable pageable, Optional<Long> cursorId);
}

// 구체적인 전략들
@Component
@Qualifier("pagingStrategy")
public class PagingStrategy implements LoadStrategy {
    // 페이징 방식 구현
}

@Component
@Qualifier("infiniteScrollStrategy")
public class InfiniteScrollStrategy implements LoadStrategy {
    // 무한스크롤 방식 구현
}

// 전략 리졸버
public class StrategyResolver {
    public static LoadStrategy resolve(List<LoadStrategy> strategies, Class<?> strategyClass) {
        // 전략 선택 로직
    }
}
```

### 프론트엔드 전략 선택
```typescript
// 전략 타입 정의
export enum LoadStrategyType {
  PAGING = 'PAGING',
  INFINITE = 'INFINITE'
}

// 전략 선택 토글 컴포넌트
const StrategyToggle: React.FC<StrategyToggleProps> = ({ strategy, onStrategyChange }) => {
  // UI 토글 구현
}
```

## API 명세

### 게시글 목록 조회
```http
GET /api/board?type=PAGING&page=0&size=3
GET /api/board?type=INFINITE&cursorId=5&size=3
```

**응답 예시:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "첫 번째 글",
      "content": "이것은 김작가의 첫 번째 게시글입니다.",
      "author": "김작가",
      "createdAt": "2024-01-01T12:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3
  },
  "totalElements": 6,
  "totalPages": 2,
  "last": false
}
```

### 단일 게시글 조회
```http
GET /api/board/1
```

### 에러 응답
```json
{
    "success": false,
    "data": null,
    "error": {
        "code": "BOARD_NOT_FOUND",
        "message": "게시글을 찾을 수 없습니다."
    }
}
```

## 실행 방법

### Docker를 이용한 실행 (권장)
```bash
# 전체 시스템 실행
docker-compose up -d

# 접속 정보
# 프론트엔드: http://localhost:3000
# 백엔드 API: http://localhost:8080
# Swagger UI: http://localhost:8080/demo-ui.html
```

### 개별 실행
```bash
# 백엔드 실행
cd board-be
./gradlew bootRun

# 프론트엔드 실행 (새 터미널)
cd board-fe
npm install
npm start
```

## 테스트

### 백엔드 테스트
```bash
cd board-be
./gradlew test
```

### 프론트엔드 테스트
```bash
cd board-fe
npm test
```

## 주요 특징

### 1. 전략패턴의 효과적 활용
- 로딩 전략을 독립적으로 구현하고 런타임에 선택 가능
- 새로운 로딩 전략 추가 시 기존 코드 수정 없이 확장 가능
- 전략별 독립적인 테스트 가능

### 2. 예외 처리 및 사용자 경험
- 구조화된 에러 응답으로 일관된 에러 처리
- 프론트엔드에서 적절한 에러 메시지 표시
- 404 에러 등 다양한 예외 상황 처리

### 3. 확장 가능한 아키텍처
- 전략패턴을 통한 유연한 설계
- RESTful API 설계로 클라이언트-서버 분리
- Docker를 통한 배포 환경 표준화

### 4. 개발 효율성
- TypeScript를 통한 타입 안정성
- Material-UI를 통한 일관된 UI/UX

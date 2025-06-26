-- Insert authors
INSERT INTO author (name, created_at, updated_at) VALUES
                                                      ('김작가', NOW(), NOW()),
                                                      ('이작가', NOW(), NOW()),
                                                      ('박작가', NOW(), NOW()),
                                                      ('최작가', NOW(), NOW()),
                                                      ('정작가', NOW(), NOW()),
                                                      ('한작가', NOW(), NOW());

-- Insert boards
INSERT INTO board (title, content, author_id, created_at, updated_at) VALUES
                                                                          ('첫 번째 글', '이것은 김작가의 첫 번째 게시글입니다.', 1, NOW(), NOW()),
                                                                          ('두 번째 글', '이것은 이작가의 게시글입니다.', 2, NOW(), NOW()),
                                                                          ('세 번째 글', '박작가가 쓴 흥미로운 글입니다.', 3, NOW(), NOW()),
                                                                          ('네 번째 글', '최작가의 유익한 글입니다.', 4, NOW(), NOW()),
                                                                          ('다섯 번째 글', '정작가의 첫 게시글입니다.', 5, NOW(), NOW()),
                                                                          ('여섯 번째 글', '한작가가 작성한 마지막 글입니다.', 6, NOW(), NOW());

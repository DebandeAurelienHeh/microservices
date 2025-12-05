CREATE TABLE reviews (
                         review_id SERIAL PRIMARY KEY,
                         product_id INT NOT NULL,
                         author VARCHAR(255),
                         subject VARCHAR(255),
                         content TEXT
);

INSERT INTO reviews (product_id, author, subject, content) VALUES
                                                               (1, 'Alice', 'Great Product', 'Really liked it!'),
                                                               (1, 'Bob', 'Not bad', 'Could be better.'),
                                                               (2, 'Charlie', 'Excellent', 'Highly recommended!'),
                                                               (3, 'Alice', 'Okay', 'Met expectations.'),
                                                               (2, 'Eve', 'Terrible', 'Did not work at all.'),
                                                               (1, 'Frank', 'Good', 'Pretty good for the price.');

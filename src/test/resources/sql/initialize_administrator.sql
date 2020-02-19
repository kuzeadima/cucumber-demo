INSERT INTO t_role(id, name)
VALUES('7c52b12d-3dcf-4ff7-805b-07848f9405c5', 'admin');

INSERT INTO t_user(id, username, password, role_id)
VALUES ('20d52d76-efbb-4168-a11d-8bd40abf174b', 'root', '$2a$10$4tTQe0m34v03L7iZ5owlf.BQZ/oKHx2BYt0JRkgfDX8PQrVl1NR8O', '7c52b12d-3dcf-4ff7-805b-07848f9405c5');

COMMIT;
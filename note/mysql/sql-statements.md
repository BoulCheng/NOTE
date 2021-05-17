# [SQL Statements](https://dev.mysql.com/doc/refman/8.0/en/sql-statements.html)


## Data Manipulation Statements
### INSERT Statement
- INSERT ... ON DUPLICATE KEY UPDATE Statement
- INSERT IGNORE INTO
- REPLACE INTO 

### Subqueries
- Subqueries with EXISTS or NOT EXISTS 
    - 在WHERE后面
    - If a subquery returns any rows at all, EXISTS subquery is TRUE, and NOT EXISTS subquery is FALSE
    - Traditionally, an EXISTS subquery starts with SELECT *, but it could begin with SELECT 5 or SELECT column1 or anything at all. MySQL ignores the SELECT list in such a subquery, so it makes no difference.
      


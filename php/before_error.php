<?php
$body = file_get_contents('php://input');
error_log($body);
http_response_code(403);
?>
BEFORE
---
<?php echo json_encode(json_decode($body), JSON_UNESCAPED_UNICODE|JSON_PRETTY_PRINT); ?>


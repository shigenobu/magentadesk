<?php
$body = file_get_contents('php://input');
error_log($body);
?>
AFTER
---
<?php echo json_encode(json_decode($body), JSON_UNESCAPED_UNICODE|JSON_PRETTY_PRINT); ?>


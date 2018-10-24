$(function() {
	common.showMessage($("#message").val());
});

function search(currentPage) {
	$("#mainForm").attr("method","GET");
	$("#mainForm").attr("action",$("#basePath").val() + "/comments");
	$("#mainForm").submit();
}

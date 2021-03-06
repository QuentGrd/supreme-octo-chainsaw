<?php include("php/function.inc.php");
if (isset($_POST['pseudo']) && isset($_POST['score'])){
	insertScore($_POST['score'], $_POST['pseudo'], 1);
}
if (!file_exists("res/lb/easy.scr")){
	createFile();
}
else{
	if ($file = fopen('res/lb/easy.scr', 'r')){
		$str = fgets($file);
		$tab = unserialize($str);
		fclose($file);
	}
	else{
		echo "Error file not open";
	}
}
?>
<!DOCTYPE html>
<html>
	<head>
		<title>Urban Life Simulator - Easy</title>
		<meta charset="utf-8">
		<link rel="stylesheet" href="css/new_style2.css">
		<?php include('include/script.inc.php'); ?>
	</head>

	<body>
		<?php include("./include/header.inc.php"); ?>

		<section>
			<h2>Classement du niveau easy</h2>
			<article id="firstArticle">
				<?php
				$i = 0;
				foreach ($tab as $key => $score) {
					echo "<p>".$key.". ".$score->getName().": ".$score->getScore();
				}
				?>
			</article>
		</section>

		<?php include('include/footer.inc.php'); ?>
	</body>
</html>
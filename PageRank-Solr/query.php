<?php

header('Content-Type: text/html; charset=utf-8');

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$algo = isset($_REQUEST['algo']) ? $_REQUEST['algo'] : 'Lucene';
$results = false;

if ($query)
{
  
  require_once('solr-php-client/Apache/Solr/Service.php');

  $solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample');

  if (get_magic_quotes_gpc() == 1)
  {
    $query = stripslashes($query);
  }

  if($algo == 'PageRank'){
    $additionalParameters = array(
      'sort' => 'pageRankFile desc'
      ); 
  }else{
    $additionalParameters = array();
  }
  try
  {
    
    $results = $solr->search($query, 0, $limit, $additionalParameters);
  }
  catch (Exception $e)
  {
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}

?>
<html>
  <head>
    <title>PHP Solr Client Example</title>
  </head>
  <body>
    <form  accept-charset="utf-8" method="get">
      <label for="q">Search:</label>
      <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
      <input type="submit"/><br><br>
      <input type="radio" name="algo" value="Lucene" <?php if ($algo == 'Lucene') echo 'checked'; ?> > Lucene 
  <input type="radio" name="algo" value="PageRank" <?php if ($algo == 'PageRank') echo 'checked'; ?>> PageRank<br>
    </form>
<?php

// display results
if ($results)
{
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);
?>
    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
    <ol>
<?php
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {
?>
      <li>
        <table style="border: 1px solid black; text-align: left">
<?php
    // iterate document fields / values
    $title = '';
    $url ='NA';
    $id = '';
    $desc = '';

    foreach ($doc as $field => $value)
    {
      if($field == 'id')
        $id = $value;
      elseif($field == 'title')
        $title = $value;
      elseif($field == 'og_url'){
        if($value == '')
          $url = 'NA';
        else
          $url = $value;
      }
      elseif($field == 'description')
        $desc = $value;
    }

?>
          <tr>
            <th>
              <?php echo htmlspecialchars('title', ENT_NOQUOTES, 'utf-8'); ?>  
            </th>
            <?php if($url == 'NA' || $url == 'None') { ?>
            <td> 
              <?php echo htmlspecialchars($title, ENT_NOQUOTES, 'utf-8'); ?>
            </td>
            <?php }else { ?>
            <td>
              <a href=<?php echo $url; ?> target="_blank" > <?php echo htmlspecialchars($title, ENT_NOQUOTES, 'utf-8'); ?> </a>
            </td>
            <?php } ?>
          </tr>
          <tr>
            <th>
              <?php echo htmlspecialchars('url', ENT_NOQUOTES, 'utf-8'); ?>
            </th>
            <?php if($url == 'NA' || $url == 'None') { ?>
            <td> 
              <?php echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8'); ?>
            </td>
            <?php }else { ?>
            <td>
              <a href=<?php echo $url; ?> target="_blank"> <?php echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8'); ?> </a>
            </td>
            <?php } ?>
          </tr>
          <tr>
            <th>
              <?php echo htmlspecialchars('id', ENT_NOQUOTES, 'utf-8'); ?>
              </th>
            <td>
             <?php echo htmlspecialchars($id, ENT_NOQUOTES, 'utf-8'); ?>
            </td>
          </tr>
          <tr>
            <th>
              <?php echo htmlspecialchars('description', ENT_NOQUOTES, 'utf-8'); ?>
            </th>
            <td>
             <?php echo htmlspecialchars($desc, ENT_NOQUOTES, 'utf-8'); ?>
             </td>
          </tr>
          
        </table>
      </li>
<?php
  }
?>
    </ol>
<?php
}
?>
  </body>
</html>


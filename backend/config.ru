require 'rack'
require 'rack/contrib/try_static'
load 'index.rb'

use Rack::TryStatic,
  :root => File.expand_path('../', __FILE__),
  :urls => %w[/], :try => ['.jpeg', '.png']

run ReadingExpert::API

dir = File.dirname(__FILE__)

$LOAD_PATH << File.join(dir, 'lib') << File.join(dir, 'config')

require 'sinatra'
require 'json'
require 'httparty'
require 'request'
require 'response'
require 'parameter_defaults'

begin
  require 'api_key'
rescue LoadError
  API_KEY = nil
  $stderr.puts 'please supply an API_KEY in config/api_key.rb'
end

set :root, dir
set :bind, '0.0.0.0'

get '/' do
  erb :index
end

get '/offers' do
  url = Request.new(API_KEY, PARAMETER_DEFAULTS, params).url
  res = Response.new(HTTParty.get(url))

  if res.error?
    erb :error, locals: {
      code: res.code,
      message: res.message
    }
  elsif res.no_content?
    erb :no_content
  else
    erb :offers, locals: {
      offers: res.offers
    }
  end
end

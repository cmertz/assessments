require 'spec_helper'
require 'rack/test'
require 'sinatra'

require './app'

setup_vcr!

describe 'app' do
  include Rack::Test::Methods

  def app
    @app ||= Sinatra::Application
  end

  describe '/' do
    it 'renders' do
      get '/'
      last_response.ok?.must_equal(true)
      last_response.body.must_match(/Offer Search/)
    end
  end

  describe '/offers' do
    %w(invalid_hashkey no_content content).each do |c|
      context c do
        it 'renders' do
          VCR.use_cassette(c) do
            get '/offers?uid=player1'
            last_response.ok?.must_equal(true)
            last_response.body.must_match(/Ruby Developer Challenge/)
          end
        end
      end
    end
  end
end

require 'grape'
require 'json'
require 'mysql'

module ReadingExpert
  class API < Grape::API

    version 'v1', using: :header, vendor: 'ReadingExpert'
    format :json

    def self.get_all_stories
      begin
        db = Mysql.real_connect('localhost', 'rails', 'kXI6cjTJCO', 'rails')
        results = db.query('SELECT * FROM Stories')

        response = []
        results.each_hash do |row|
          response.push(row)
        end
        response

      ensure
        db.close       
      end
    end

    resource :stories do
      desc "IDs and titles of all stories"
      get :get_all_stories do
    	API.get_all_stories
      end
    end

  end
end

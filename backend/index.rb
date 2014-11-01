require 'grape'
require 'json'

module ReadingExpert
  class API < Grape::API

    version 'v1', using: :header, vendor: 'ReadingExpert'
    format :json

    def self.get_all_stories
      stories = [{id: 123, title: "Lion"}, {id: 124, title: "Snake"}]
    end

    resource :stories do
      desc "IDs and titles of all stories"
      get :get_all_stories do
    	API.get_all_stories
      end
    end

  end
end

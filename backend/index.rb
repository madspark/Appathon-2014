require 'grape'
require 'json'
require 'mysql'
require 'open-uri'

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
          row['description'] = row['content'].split(".")[0] + "."
          response.push(row)
        end
        response

      ensure
        db.close       
      end
    end

    def self.get_all_questions
      begin
        db = Mysql.real_connect('localhost', 'rails', 'kXI6cjTJCO', 'rails')
        results = db.query('SELECT * FROM Questions')

        response = []
        results.each_hash do |row|
          response.push(row)
        end
        response

      ensure
        db.close
      end
    end

    def self.get_questions_by_id_where(id, argument)
      begin
        db = Mysql.real_connect('localhost', 'rails', 'kXI6cjTJCO', 'rails')
 
        id = id.to_s
        query = 'SELECT * FROM Questions WHERE story_id = ' + id + ' AND ' + argument

        results = db.query(query)

        response = []
        results.each_hash do |row|
          response.push(row)
        end
        response
      ensure
        db.close
      end
    end

    def self.get_definition(word)
      url = 'http://dictionary.cambridge.org/search/british/direct/?q=' + word

      begin
      open(url) do |f|
        page_string = f.read
 	     
        start_index = page_string.index("<span class=\"def\">")
	if start_index == nil
          {:definition => "Sorry, no definition found."}
        else
          stop_index = page_string.index("</span>", start_index)
      
          page_string = page_string[start_index..stop_index]
        
          result = ''
          open_bracket = false
          page_string.each_char do |chr|
            if chr == '<'
              open_bracket = true
            end
            if !open_bracket
              result += chr
            end
            if chr == '>'
              open_bracket = false
            end
          end
          result.rstrip!
          
          if result[-1] == ":"
            result[-1] = "."
          else
            result += "."
          end

          result[0] = result[0].capitalize
          {:definition => result}
        end
      end
      rescue 
        {:definition => "Sorry, no definition found."}
      end
    end


    resource :stories do
      get :get_all_stories do
    	API.get_all_stories
      end
      
      get :get_all_questions do
        API.get_all_questions
      end
      
      params do
        requires :id, type: Integer
      end
      get :questions_by_id do
        API.get_questions_by_id_where(params['id'], 'LENGTH(correct_answer) > 6')
      end

      get :crossword_questions_by_id do
        API.get_questions_by_id_where(params['id'], 'LENGTH(correct_answer) <= 6')
      end

    end

    resource :definitions do
      params do
        requires :word, type: String
      end
      route_param :get_definition do
        get do 
          API.get_definition(params['word'])
        end
      end

    end

  end
end

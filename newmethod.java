        // Merge categories with the same ID and format them
            List<Map<String, Object>> formattedCategories = categoriesByIdMap.values().stream()
                    .map(categoriesWithSameId -> formatCategoryGroupFiltered(categoriesWithSameId, fiscal_years, populations))
                    .collect(Collectors.toList());
            
            // For certain category types, filter entire category objects by population label
            // These types have one category object per population (label = population code)
            if (populations != null && !populations.isEmpty() && 
                (categoryType.equals("participation_rates") || 
                 categoryType.equals("arrival_assumptions") || 
                 categoryType.equals("arrival_distributions"))) {
                
                final List<String> upperPopulations = populations.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
                
                formattedCategories = formattedCategories.stream()
                        .filter(category -> {
                            Object labelObj = category.get("label");
                            if (labelObj != null) {
                                String label = labelObj.toString().toUpperCase();
                                boolean matches = upperPopulations.contains(label);
                                logger.debug("Filtering category object with label '{}' for type '{}': {}", 
                                           labelObj, categoryType, matches);
                                return matches;
                            }
                            return true; // Keep if no label
                        })
                        .collect(Collectors.toList());
            }